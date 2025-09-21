package io.github.abizerr.quickedit.engine.drawspec

import androidx.annotation.FloatRange

sealed interface ShapeSpec {
    val argb: Int
    val alpha: Float    // 0f..1f
    val widthPx: Float
    val isEraser: Boolean get() = false

    data class Brush(
        val points: List<Pair<Float, Float>>,
        override val argb: Int,
        override val alpha: Float,
        override val widthPx: Float,
        override val isEraser: Boolean = false
    ) : ShapeSpec

    data class Shape(
        val shapeType: ShapeType,
        val startOffset: Pair<Float, Float>,
        val endOffset: Pair<Float, Float>,
        override val argb: Int,
        override val alpha: Float,
        override val widthPx: Float,
    ) : ShapeSpec
}
