package io.github.abizerr.quickedit.engine.impl

import io.github.abizerr.quickedit.engine.api.*

/**
 * Minimal, synchronous engine to keep Phase 4 buildable.
 * Phase 5 will add real bitmap ops, curves, crop, etc.
 */
public class DefaultEditEngine(
    private val maxUndo: Int = 20
) : EditEngine {

    private val historyManager = HistoryManager(maxUndo)

    override val historyView: HistoryView = object : HistoryView {
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
            else -> current.copy(rev = current.rev + 1) // TODO (revamp): placeholder; real ops later
        }
        if (op !is EditOp.Undo && op !is EditOp.Redo) historyManager.push(next)
        return next
    }

    override suspend fun render(snapshot: EditSnapshot, size: Size): RenderResult {
        // TODO Phase 5: produce a preview bitmap
        return RenderResult(ok = true)
    }

    override suspend fun save(snapshot: EditSnapshot, format: SaveFormat): Result<EditedImage> {
        // TODO: Phase 5: encode bitmap; for now, return a dummy EditedImage
        val mime = when (format) {
            is SaveFormat.Png -> "image/png"
            is SaveFormat.Jpeg -> "image/jpeg"
            is SaveFormat.WebP -> if (format.lossless) "image/webp" else "image/webp"
        }
        return Result.success(EditedImage(mimeType = mime, bytes = null))
    }
}
