package com.abizer_r.touchdraw.ui.textMode

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import com.abizer_r.touchdraw.ui.transformableViews.base.TransformableTextBoxState
import com.abizer_r.touchdraw.ui.transformableViews.base.TransformableBoxState


sealed class TextModeEvent {
    data class UpdateTextFieldValue(val textInput: String): TextModeEvent()
    data class UpdateTextFont(val textFont: TextUnit): TextModeEvent()
    data class SelectTextColor(val index: Int, val color: Color): TextModeEvent()
    data class SelectTextAlign(val index: Int, val textAlign: TextAlign): TextModeEvent()
    data class UpdateTransformableViewsList(val list: ArrayList<TransformableBoxState>): TextModeEvent()
    data class AddTransformableTextBox(val textBoxState: TransformableTextBoxState): TextModeEvent()
    data class ShowTextField(val textFieldState: TextModeState.TextFieldState): TextModeEvent()
    object HideTextField: TextModeEvent()
}