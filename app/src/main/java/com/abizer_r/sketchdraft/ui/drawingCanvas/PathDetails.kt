package com.abizer_r.sketchdraft.ui.drawingCanvas

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path

data class PathDetails(
    val path: Path,
    val width: Float,
    val alpha: Float,
    val color: Color = Color.Blue
)
