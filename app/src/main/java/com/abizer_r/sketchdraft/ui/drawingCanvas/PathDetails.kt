package com.abizer_r.sketchdraft.ui.drawingCanvas

import androidx.compose.ui.graphics.Color
import com.abizer_r.sketchdraft.ui.drawingCanvas.shapes.Shape

data class PathDetails(
    val shape: Shape,
    val paintValues: PaintValues
)

data class PaintValues(
    val color: Color,
    val width: Float,
    val alpha: Float
)
