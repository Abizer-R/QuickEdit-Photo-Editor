package com.abizer_r.touchdraw.ui.drawingCanvas

import androidx.compose.ui.graphics.Color
import com.abizer_r.touchdraw.ui.drawingCanvas.drawingTool.DrawingTool
import com.abizer_r.touchdraw.ui.drawingCanvas.models.PathDetails
import java.util.Stack

data class DrawingState (
    val strokeWidth: Int,
    val strokeColor: Color,
    val drawingTool: DrawingTool,
    val opacity: Int,
    val pathDetailStack: Stack<PathDetails>,
    val redoStack: Stack<PathDetails>
)