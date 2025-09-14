package io.github.abizerr.quickedit.engine.impl

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build
import io.github.abizerr.quickedit.engine.api.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.core.graphics.scale
import java.io.ByteArrayOutputStream

/**
 * Minimal but real engine:
 * - Decodes source (Uri/Bitmap)
 * - Renders a scaled preview Bitmap
 * - Saves to bytes (PNG/JPEG/WebP)
 */
class DefaultEditEngine(
    private val maxUndo: Int = 20,
    private val resolver: ContentResolver? = null
) : EditEngine {

    private val historyManager = HistoryManager(maxUndo)

    override val history: HistoryView = object : HistoryView {
        override val canUndo: Boolean get() = this@DefaultEditEngine.historyManager.canUndo()
        override val canRedo: Boolean get() = this@DefaultEditEngine.historyManager.canRedo()
        override val undoCount: Int get() = this@DefaultEditEngine.historyManager.undoCount()
        override val redoCount: Int get() = this@DefaultEditEngine.historyManager.redoCount()
    }

    override suspend fun newSession(image: EditImage): EditSnapshot {
        val snap = EditSnapshot(image = image, rev = 0)
        historyManager.setInitial(snap)
        return snap
    }

    override suspend fun apply(op: EditOp): EditSnapshot {
        val current = historyManager.current() ?: error("Call newSession() first")
        val next = when (op) {
            is EditOp.Undo -> historyManager.undo() ?: current
            is EditOp.Redo -> historyManager.redo() ?: current
            is EditOp.ImageCropped -> current.copy(
                image = EditImage.FromBitmap(op.croppedBitmap),
                rev = current.rev + 1
            )
            else -> current.copy(rev = current.rev + 1) // TODO (revamp): placeholder; real ops later
        }
        if (op !is EditOp.Undo && op !is EditOp.Redo) historyManager.push(next)
        return next
    }

    override suspend fun render(snapshot: EditSnapshot, size: Size): RenderResult {
        val bitmap = withContext(Dispatchers.Default) {
            val baseBitmap = decode(snapshot.image) ?: return@withContext null
            scaleToFit(baseBitmap, size.width, size.height)
        } ?: return RenderResult(ok = false, preview = null)

        return RenderResult(ok = true, preview = bitmap)
    }

    override suspend fun save(snapshot: EditSnapshot, format: SaveFormat): Result<EditedImage> = withContext(Dispatchers.IO) {
        val baseBitmap = decode(snapshot.image)
            ?: return@withContext Result.failure(IllegalStateException("Decode failed"))

        val (compressFormat, quality, mime) = when (format) {
            is SaveFormat.Png -> Triple(Bitmap.CompressFormat.PNG, 100, "image/png")
            is SaveFormat.Jpeg -> Triple(Bitmap.CompressFormat.JPEG, format.quality.coerceIn(0, 100), "image/jpeg")
            is SaveFormat.WebP -> {
                val q = format.quality.coerceIn(0, 100)
                val mimeType = "image/webp"
                val compress = if (Build.VERSION.SDK_INT >= 30) {
                    if (format.lossless) Bitmap.CompressFormat.WEBP_LOSSLESS else Bitmap.CompressFormat.WEBP_LOSSY
                } else {
                    Bitmap.CompressFormat.WEBP
                }
                Triple(compress, q, mimeType)
            }
        }

        val outputStream = ByteArrayOutputStream()
        baseBitmap.compress(compressFormat, quality, outputStream)
        Result.success(EditedImage(
            mimeType = mime,
            bytes = outputStream.toByteArray()
        ))
    }

    private fun decode(image: EditImage): Bitmap? {
        return when (image) {
            is EditImage.FromBitmap -> image.bitmap
            is EditImage.FromUri -> {
                decodeBitmapFromUri(image)
            }
        }
    }

    private fun decodeBitmapFromUri(
        image: EditImage.FromUri
    ): Bitmap? = try {
        val contentResolver = resolver ?: return null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // ImageDecoder handles EXIF orientation, wide color, scaling hints
            val src = ImageDecoder.createSource(contentResolver, image.uri)
            ImageDecoder.decodeBitmap(src)
        } else {
            // BitmapFactory.decodeStream doesn't auto-rotate based on EXIF
            // But, it is the only safe option we have below API-P
            contentResolver.openInputStream(image.uri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream)
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    private fun scaleToFit(src: Bitmap, targetW: Int, targetH: Int): Bitmap {
        if (targetW <= 0 || targetH <= 0)   return src
        val wScale = targetW.toFloat() / src.width
        val hScale = targetH.toFloat() / src.height
        val scale = minOf(wScale, hScale)
        val w = (src.width * scale).toInt().coerceAtLeast(1)
        val h = (src.height * scale).toInt().coerceAtLeast(1)
        return src.scale(w, h)
    }
}
