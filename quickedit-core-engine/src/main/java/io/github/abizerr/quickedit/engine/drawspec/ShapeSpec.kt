package io.github.abizerr.quickedit.engine.drawspec

import androidx.annotation.FloatRange

sealed interface ShapeSpec {

    companion object {
        val defaultOffset = Pair(Float.NaN, Float.NaN)
    }
    val argb: Int
    val alpha: Float    // 0f..1f
    val widthPx: Float
    val isEraser: Boolean get() = false

    data class Brush(
        val points: MutableList<Pair<Float, Float>> = mutableListOf(),
        override val argb: Int,
        @FloatRange(from = 0.0, to = 1.0) override val alpha: Float,
        override val widthPx: Float,
        override val isEraser: Boolean = false
    ) : ShapeSpec

    data class Shape(
        val shapeType: ShapeType,
        var startOffset: Pair<Float, Float> = defaultOffset,
        var endOffset: Pair<Float, Float> = defaultOffset,
        override val argb: Int,
        @FloatRange(from = 0.0, to = 1.0) override val alpha: Float,
        override val widthPx: Float,
    ) : ShapeSpec
}
