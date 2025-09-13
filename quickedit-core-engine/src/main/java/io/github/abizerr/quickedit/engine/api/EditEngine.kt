package io.github.abizerr.quickedit.engine.api

interface EditEngine {
    suspend fun newSession(image: EditImage): EditSnapshot
    suspend fun apply(op: EditOp): EditSnapshot
    suspend fun render(snapshot: EditSnapshot, size: Size): RenderResult
    suspend fun save(snapshot: EditSnapshot, format: SaveFormat): Result<EditedImage>
    val historyView: HistoryView
    /**
     * FUTURE-1: Capabilities & queries (non-breaking):
     * fun supports(op: kotlin.reflect.KClass<out EditOp>): Boolean = true
     * val maxCanvasSize: Size get() = Size(8192, 8192)
     * FUTURE-2: Observability hooks:
     * var events: ((EngineEvent) -> Unit)?
     */
}

interface HistoryView {
    val canUndo: Boolean
    val canRedo: Boolean
    val undoCount: Int
    val redoCount: Int
    /**
     * FUTURE: Expose memory footprint if required
     * val approxMemoryBytes: Long get() = 0
     */
}


/**
 *  FUTURE: Emit non-fatal diagnostics without throwing:
 *  sealed interface EngineEvent {
 *    data class OpLatency(val op: EditOp, val ms: Long): EngineEvent
 *    data class RenderDownscaled(val requested: Size, val actual: Size): EngineEvent
 *    data class OOMPrevented(val reducedHistory: Int): EngineEvent
 *  }
 */