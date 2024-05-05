package com.abizer_r.touchdraw.ui.drawingCanvas.drawingTool.shapes

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.abizer_r.touchdraw.ui.drawingCanvas.models.PaintValues

class LineShape: DrawingShape {
    private var startOffset: Offset = Offset.Unspecified
    private var endOffset: Offset = Offset.Unspecified

    override fun draw(drawScope: DrawScope, paintValues: PaintValues) {
        drawScope.drawLine(
            start = startOffset,
            end = endOffset,
            brush = SolidColor(paintValues.color),
            strokeWidth = paintValues.width,
            cap = StrokeCap.Round,
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