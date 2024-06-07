package com.abizer_r.touchdraw.ui.textMode.stateHandling

import com.abizer_r.touchdraw.ui.transformableViews.base.TransformableTextBoxState
import com.abizer_r.touchdraw.ui.transformableViews.base.TransformableBoxState


sealed class TextModeEvent {
    data class UpdateTextFieldValue(val textInput: String): TextModeEvent()
    data class UpdateTransformableViewsList(val list: ArrayList<TransformableBoxState>): TextModeEvent()
    data class AddTransformableTextBox(val textBoxState: TransformableTextBoxState): TextModeEvent()
    data class ShowTextField(val textFieldState: TextModeState.TextFieldState): TextModeEvent()
    object HideTextField: TextModeEvent()
}