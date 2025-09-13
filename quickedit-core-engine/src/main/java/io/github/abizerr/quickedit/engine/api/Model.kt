package io.github.abizerr.quickedit.engine.api

sealed interface EditOp

data class EditSnapshot(val rev: Long = 0)

data class Size(val width: Int, val height: Int)

sealed interface SaveFormat {
    data class Png(val lossless: Boolean = true) : SaveFormat
    data class Jpeg(val quality: Int = 90) : SaveFormat
    data class WebP(val lossless: Boolean = false, val quality: Int = 90) : SaveFormat
}

sealed interface Output

object RenderResult

object SaveResult

interface HistoryView {
    val canUndo: Boolean
    val canRedo: Boolean
    val undoCount: Int
    val redoCount: Int
}
