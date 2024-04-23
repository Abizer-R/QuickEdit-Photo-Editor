package com.abizer_r.sketchdraft.ui.drawingCanvas.controllerBottomSheet

import androidx.compose.ui.graphics.Color

data class ControllerBSState(
    val opacity: Int,
    val strokeWidth: Int,
    val drawMode: DrawMode,
    val colorList: List<Color>,
    val selectedColorIndex: Int,
    val isUndoEnabled: Boolean,
    val isRedoEnabled: Boolean
)

enum class DrawMode {
    BRUSH, LINE, OVAL, RECTANGLE, ERASER
}

fun ControllerBSState.getSelectedColor() = colorList[selectedColorIndex]