package com.abizer_r.touchdraw.ui.drawMode.stateHandling

import com.abizer_r.touchdraw.ui.drawMode.drawingCanvas.models.PathDetails
import java.util.Stack

data class TextModeState(
    val isTextFieldVisible: Boolean = false,
    val textFieldValue: String = "",
    val recompositionTrigger: Long = 0

//    val showBottomToolbarExtension: Boolean = false,
//    val pathDetailStack: Stack<PathDetails> = Stack(),
//    val redoStack: Stack<PathDetails> = Stack(),

)