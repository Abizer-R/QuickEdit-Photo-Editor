package io.github.abizerr.quickedit.engine.api

import android.graphics.Bitmap
import android.graphics.Rect

/** Minimum set; expand in Phase 6 with tool packs. */
sealed interface EditOp {
    data class ApplyCurve(val presetId: String) : EditOp
    data class CropImage(val rect: Rect) : EditOp
    data class InsertText(val text: String, val x: Float, val y: Float) : EditOp
    data class DrawPath(val points: List<Pair<Float, Float>>, val width: Float) : EditOp
    data object Undo : EditOp
    data object Redo : EditOp
    /**
     * FUTURE: Add ops additively; avoid changing existing ones:
     * example: Transform, Blend, Perspective, InsertImage, InsertSticker, Select, ClearSelection, etc.
     */
}
