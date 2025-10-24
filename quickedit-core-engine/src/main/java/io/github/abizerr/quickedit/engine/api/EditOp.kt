package io.github.abizerr.quickedit.engine.api

import android.graphics.Rect
import android.graphics.RectF
import io.github.abizerr.quickedit.engine.drawspec.ShapeSpec

/** Minimum set; expand in Phase 6 with tool packs. */
sealed interface EditOp {
//    data class ApplyCurve(val presetId: String) : EditOp
    data class CropImage(val rect: Rect) : EditOp
//    data class InsertText(val text: String, val x: Float, val y: Float) : EditOp
    data object Undo : EditOp
    data object Redo : EditOp

    data class DrawShape(val spec: ShapeSpec)
}
