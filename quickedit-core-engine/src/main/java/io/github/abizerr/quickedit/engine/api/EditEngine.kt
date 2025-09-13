package io.github.abizerr.quickedit.engine.api

interface EditEngine {
  suspend fun apply(op: EditOp): EditSnapshot
  suspend fun render(snapshot: EditSnapshot, size: Size): RenderResult
  suspend fun save(snapshot: EditSnapshot, format: SaveFormat, out: Output): SaveResult
  val history: HistoryView
}
