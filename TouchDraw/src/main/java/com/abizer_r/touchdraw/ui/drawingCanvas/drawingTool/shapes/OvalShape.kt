package com.abizer_r.touchdraw.ui.drawingCanvas.drawingTool.shapes

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

class OvalShape(
    color: Color? = null,
    width: Float? = null,
    alpha: Float? = null
): AbstractShape() {

    init {
        updatePaintValues(color, width, alpha)
    }

    private var startOffset: Offset = Offset.Unspecified
    private var endOffset: Offset = Offset.Unspecified

    override fun draw(drawScope: DrawScope) {
        drawScope.drawOval(
            topLeft = startOffset,
            size = Size(
                width = (endOffset.x - startOffset.x),
                height = (endOffset.y - startOffset.y)
            ),
            brush = SolidColor(mColor),
            style = Stroke(
                width = mWidth,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            ),
            alpha = mAlpha
        )
    }

    override fun initShape(startX: Float, startY: Float) {
        startOffset = Offset(startX, startY)
    }

    override fun moveShape(endX: Float, endY: Float) {
        endOffset = Offset(endX, endY)
    }

    override fun shouldDraw(): Boolean {
        return startOffset != Offset.Unspecified && endOffset != Offset.Unspecified
    }
}