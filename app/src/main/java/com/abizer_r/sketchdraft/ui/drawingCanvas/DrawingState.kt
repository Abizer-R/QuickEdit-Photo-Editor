package com.abizer_r.sketchdraft.ui.drawingCanvas

import androidx.compose.ui.graphics.Color
import com.abizer_r.sketchdraft.ui.drawingCanvas.controllerBottomSheet.DrawMode
import com.abizer_r.touchdraw.ui.drawingCanvas.PathDetails
import java.util.Stack

data class DrawingState (
    val strokeWidth: Int,
    val strokeColor: Color,
    val drawMode: DrawMode,
    val opacity: Int,
    val pathDetailStack: Stack<PathDetails>,
    val redoStack: Stack<PathDetails>
)