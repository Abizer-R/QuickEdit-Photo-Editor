package com.abizer_r.sketchdraft.ui.drawingCanvas.controllerBottomSheet

import androidx.compose.ui.graphics.Color

data class ControllerBSState(
    val opacity: Int,
    val strokeWidth: Int,
    val colorList: List<Color>,
    val selectedColorIndex: Int,
    val isUndoEnabled: Boolean,
    val isRedoEnabled: Boolean
)

fun ControllerBSState.getSelectedColor() = colorList[selectedColorIndex]