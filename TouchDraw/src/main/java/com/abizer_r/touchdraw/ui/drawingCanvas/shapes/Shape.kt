package com.abizer_r.touchdraw.ui.drawingCanvas.shapes

import androidx.compose.ui.graphics.drawscope.DrawScope
import com.abizer_r.touchdraw.ui.drawingCanvas.PaintValues

interface Shape {
    fun draw(drawScope: DrawScope, paintValues: PaintValues)
    fun initShape(startX: Float, startY: Float)
    fun moveShape(endX: Float, endY: Float)
    fun shouldDraw(): Boolean
}