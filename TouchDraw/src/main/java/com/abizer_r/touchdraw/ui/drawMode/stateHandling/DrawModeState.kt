package com.abizer_r.touchdraw.ui.drawMode.stateHandling

import androidx.compose.ui.graphics.Color
import com.abizer_r.touchdraw.ui.drawMode.drawingCanvas.models.PathDetails
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.state.BottomToolbarItem
import java.util.Stack

data class DrawModeState(
    val showColorPicker: Boolean = false,
    val selectedColor: Color = Color.White,
    val selectedTool: BottomToolbarItem = BottomToolbarItem.NONE,
    val showBottomToolbarExtension: Boolean = false,
    val pathDetailStack: Stack<PathDetails> = Stack(),
    val redoStack: Stack<PathDetails> = Stack(),
    val recompositionTrigger: Long = 0
)