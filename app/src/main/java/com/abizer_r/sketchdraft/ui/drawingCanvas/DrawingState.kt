package com.abizer_r.sketchdraft.ui.drawingCanvas

import androidx.compose.ui.graphics.Color

data class DrawingState (
    val strokeWidth: Int,
    val strokeColor: Color,
    val opacity: Int,
    val pathDetailList: ArrayList<PathDetails>
)