package io.github.abizerr.quickedit.tool.draw.ui

import androidx.compose.ui.graphics.Color
import io.github.abizerr.quickedit.tool.draw.models.DrawToolItem
import io.github.abizerr.quickedit.tool.draw.models.PathDetails
import java.util.Stack

data class DrawModeState(
    val showColorPicker: Boolean = false,
    val selectedColor: Color = Color.Companion.White,
    val selectedTool: DrawToolItem = DrawToolItem.NONE,
    val showBottomToolbarExtension: Boolean = false,
    val pathDetailStack: Stack<PathDetails> = Stack(),
    val redoStack: Stack<PathDetails> = Stack(),
    val recompositionTrigger: Long = 0
)