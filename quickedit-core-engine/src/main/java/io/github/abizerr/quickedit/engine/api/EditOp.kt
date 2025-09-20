package io.github.abizerr.quickedit.engine.api

import android.graphics.Rect
import android.graphics.RectF

/** Minimum set; expand in Phase 6 with tool packs. */
sealed interface EditOp {
//    data class ApplyCurve(val presetId: String) : EditOp
    data class CropImage(val rect: Rect) : EditOp
//    data class InsertText(val text: String, val x: Float, val y: Float) : EditOp
//    data class DrawPath(val points: List<Pair<Float, Float>>, val width: Float) : EditOp
    data object Undo : EditOp
    data object Redo : EditOp

    data class DrawStroke(
        val points: List<Pair<Float, Float>>,
        val widthPx: Float,
        val argb: Int,
        val opacity: Float,     // 0f..1f
        val erase: Boolean
    ) : EditOp

    data class DrawLine(
        val start: Pair<Float, Float>,
        val end: Pair<Float, Float>,
        val widthPx: Float,
        val argb: Int,
        val opacity: Float,
        val erase: Boolean
    ) : EditOp


    data class DrawRect(
        val bounds: RectF,
        val widthPx: Float,
        val argb: Int,
        val opacity: Float,
        val erase: Boolean
    ) : EditOp

    data class DrawOval(
        val bounds: RectF,
        val widthPx: Float,
        val argb: Int,
        val opacity: Float,
        val erase: Boolean
    ) : EditOp

}
