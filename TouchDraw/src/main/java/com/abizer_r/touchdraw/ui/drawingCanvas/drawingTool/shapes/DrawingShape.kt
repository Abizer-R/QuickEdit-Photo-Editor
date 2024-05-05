package com.abizer_r.touchdraw.ui.drawingCanvas.drawingTool.shapes

import androidx.compose.ui.graphics.drawscope.DrawScope
import com.abizer_r.touchdraw.ui.drawingCanvas.models.PaintValues

interface DrawingShape {
    fun draw(drawScope: DrawScope, paintValues: PaintValues)
    fun initShape(startX: Float, startY: Float)
    fun moveShape(endX: Float, endY: Float)
    fun shouldDraw(): Boolean
}