package com.abizer_r.touchdraw.ui.drawMode.stateHandling

import com.abizer_r.touchdraw.ui.drawMode.drawingCanvas.models.PathDetails
import java.util.Stack

data class DrawModeState(
    val showColorPicker: Boolean = false,
    val showBottomToolbarExtension: Boolean = false,
    val pathDetailStack: Stack<PathDetails> = Stack(),
    val redoStack: Stack<PathDetails> = Stack(),
    val recompositionTrigger: Long = 0
)