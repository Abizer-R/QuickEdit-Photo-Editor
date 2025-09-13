package io.github.abizerr.quickedit.engine.api


/** A lightweight reference to the image the editor works on. */
sealed interface EditImage {
    data class FromUri(val uri: android.net.Uri) : EditImage
    data class FromBitmap(val bitmap: android.graphics.Bitmap) : EditImage
}

/** Immutable snapshot of the editing graph at a point in time. */
data class EditSnapshot(
    val image: EditImage,
    val rev: Long = 0L
)

/** Output from save(). You can extend later with Uri/bytes/metadata. */
data class EditedImage(
    val mimeType: String,
    val bytes: ByteArray? = null
)

/** Sizes used by render() */
data class Size(val width: Int, val height: Int)

/** Render output placeholder (Phase 5 will hold a Bitmap or ImageBitmap). */
data class RenderResult(val ok: Boolean)