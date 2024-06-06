package com.abizer_r.touchdraw.ui.drawMode.drawingCanvas

import androidx.compose.ui.graphics.Color
import com.abizer_r.touchdraw.ui.drawMode.drawingCanvas.drawingTool.DrawingTool
import com.abizer_r.touchdraw.ui.drawMode.drawingCanvas.models.PathDetails
import java.util.Stack

data class DrawingCanvasState (
    val strokeWidth: Int,
    val strokeColor: Color,
    val drawingTool: DrawingTool,
    val opacity: Int,
    val pathDetailStack: Stack<PathDetails>,
    val redoStack: Stack<PathDetails>
)