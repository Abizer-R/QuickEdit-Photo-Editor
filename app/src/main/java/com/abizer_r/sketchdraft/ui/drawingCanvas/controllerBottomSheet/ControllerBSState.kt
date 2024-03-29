package com.abizer_r.sketchdraft.ui.drawingCanvas.controllerBottomSheet

import androidx.compose.ui.graphics.Color

data class ControllerBSState(
    val opacity: Int,
    val strokeWidth: Int,
    val colorList: List<Color>,
    val selectedColorIndex: Int
)

fun ControllerBSState.getSelectedColor() = colorList[selectedColorIndex]