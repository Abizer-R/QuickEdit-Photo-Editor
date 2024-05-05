package com.abizer_r.touchdraw.ui.drawingCanvas.models

import com.abizer_r.touchdraw.ui.drawingCanvas.drawingTool.shapes.DrawingShape

data class PathDetails(
    val drawingShape: DrawingShape,
    val paintValues: PaintValues
)