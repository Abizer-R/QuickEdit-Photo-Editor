package io.github.abizerr.quickedit.engine.drawspec

data class PaintSpec(
    val argb: Int,
    val alpha: Float,
    val widthPx: Float,
    val isEraser: Boolean
)

fun ShapeSpec.toPaintSpec(): PaintSpec = PaintSpec(
    argb = this.argb,
    alpha = this.alpha.coerceIn(0f,1f),
    widthPx = this.widthPx.coerceAtLeast(0.5f),
    isEraser = this.isEraser
)