package com.abizer_r.sketchdraft.ui.drawingCanvas.shapes

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.abizer_r.sketchdraft.ui.drawingCanvas.PaintValues

class OvalShape: Shape {
    private var startOffset: Offset = Offset.Unspecified
    private var endOffset: Offset = Offset.Unspecified

    override fun draw(drawScope: DrawScope, paintValues: PaintValues) {
        drawScope.drawOval(
            topLeft = startOffset,
            size = Size(
                width = (endOffset.x - startOffset.x),
                height = (endOffset.y - startOffset.y)
            ),
            brush = SolidColor(paintValues.color),
            style = Stroke(
                width = paintValues.width,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            ),
            alpha = paintValues.alpha
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