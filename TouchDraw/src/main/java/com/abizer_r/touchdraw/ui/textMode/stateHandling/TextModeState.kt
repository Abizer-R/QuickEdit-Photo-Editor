package com.abizer_r.touchdraw.ui.textMode.stateHandling

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.abizer_r.components.util.ColorUtils
import com.abizer_r.touchdraw.ui.transformableViews.base.TransformableBoxState

data class TextModeState(
    val textFieldState: TextFieldState = TextFieldState(),
//    val textFieldValue: String = "",
    val transformableViewStateList: ArrayList<TransformableBoxState> = arrayListOf(),
    val shouldGoToNextScreen: Boolean = false,
    val recompositionTrigger: Long = 0
) {
    data class TextFieldState(
        val isVisible: Boolean = true,
        val textStateId: String? = null,
        val shouldRequestFocus: Boolean = true, /* initial value is true as the textField is visible initially */
        val text: String = "",
        val textAlign: TextAlign = TextAlign.Center,
        val textColorList: ArrayList<Color> = ColorUtils.defaultColorList,
        val selectedColorIndex: Int = 0
    )
}

fun TextModeState.TextFieldState.getSelectedColor(): Color {
    if (selectedColorIndex < 0 || selectedColorIndex >= textColorList.size) {
        throw IndexOutOfBoundsException("selectedIndex = $selectedColorIndex out of bound, textColorSize = ${textColorList.size}")
    }
    return textColorList[selectedColorIndex]
}

fun TextModeState.TextFieldState.getIndexFromColor(color: Color): Int {
    val indexOfColor = textColorList.indexOfFirst { it == color }
    if (indexOfColor == -1) {
        throw IllegalArgumentException("color not present in the textColorList")
    }
    return indexOfColor
}