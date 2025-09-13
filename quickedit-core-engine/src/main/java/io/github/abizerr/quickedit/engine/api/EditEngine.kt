package io.github.abizerr.quickedit.engine.api

interface EditEngine {
  suspend fun newSession(image: EditImage): EditSnapshot
  suspend fun apply(op: EditOp): EditSnapshot
  suspend fun render(snapshot: EditSnapshot, size: Size): RenderResult
  suspend fun save(snapshot: EditSnapshot, format: SaveFormat): Result<EditedImage>
  val historyView: HistoryView
}

interface HistoryView {
  val canUndo: Boolean
  val canRedo: Boolean
  val undoCount: Int
  val redoCount: Int
}
