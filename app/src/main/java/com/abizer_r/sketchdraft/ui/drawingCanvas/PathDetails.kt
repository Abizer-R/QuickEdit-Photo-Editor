package com.abizer_r.sketchdraft.ui.drawingCanvas

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import com.abizer_r.sketchdraft.ui.drawingCanvas.controllerBottomSheet.StrokeMode

data class PathDetails(
    val path: Path,
    val width: Float,
    val alpha: Float,
    val color: Color = Color.Blue,
    val strokeMode: StrokeMode
)
