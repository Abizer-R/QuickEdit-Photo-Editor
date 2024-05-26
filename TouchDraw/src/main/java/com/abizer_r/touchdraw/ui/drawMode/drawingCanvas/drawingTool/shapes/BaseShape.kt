package com.abizer_r.touchdraw.ui.drawMode.drawingCanvas.drawingTool.shapes

import androidx.compose.ui.graphics.drawscope.DrawScope

interface BaseShape {
    fun draw(drawScope: DrawScope)
    fun initShape(startX: Float, startY: Float)
    fun moveShape(endX: Float, endY: Float)
    fun shouldDraw(): Boolean
}