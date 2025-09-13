package io.github.abizerr.quickedit.engine.api

import android.graphics.Bitmap


/** A lightweight reference to the image the editor works on. */
sealed interface EditImage {
    data class FromUri(val uri: android.net.Uri) : EditImage
    data class FromBitmap(val bitmap: android.graphics.Bitmap) : EditImage
    /**
     * FUTURE: Possible additions without breaking callers:
     * example: FromBytes, FromFile, FromHardwareBuffer, ContentProvider, etc.
     */
}

/** Immutable snapshot of the editing graph at a point in time. */
data class EditSnapshot(
    val image: EditImage,
    val rev: Long = 0L
    /**
     * FUTURE: Add graph states without breaking callers:
     * example: Layers, Selection, Metadata
     */
)

/** Output from save(). You can extend later with Uri/bytes/metadata. */
data class EditedImage(
    val mimeType: String,
    val bytes: ByteArray? = null
    /**
     * FUTURE: Add destination/EXIF without breaking callers:
     * example: savedFile: android.net.Uri, exif: Map<String, String>
     */
)

/** Sizes used by render() */
data class Size(val width: Int, val height: Int)

/** Render output placeholder (Phase 5 will hold a Bitmap or ImageBitmap). */
data class RenderResult(
    val ok: Boolean,
    val preview: Bitmap
)
/**
 * FUTURE: Add preview payloads without chaning the signature of render() in EditEngine interface:
 * example: bitmap, downscaleFactor, etc.
 */