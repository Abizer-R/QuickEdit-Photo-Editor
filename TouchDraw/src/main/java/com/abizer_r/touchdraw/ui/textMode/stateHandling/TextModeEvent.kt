package com.abizer_r.touchdraw.ui.textMode.stateHandling

import androidx.compose.ui.graphics.Color
import com.abizer_r.touchdraw.ui.transformableViews.base.TransformableTextBoxState
import com.abizer_r.touchdraw.ui.transformableViews.base.TransformableBoxState


sealed class TextModeEvent {
    data class UpdateTextFieldValue(val textInput: String): TextModeEvent()
    data class SelectTextColor(val index: Int, val color: Color): TextModeEvent()
    data class UpdateTransformableViewsList(val list: ArrayList<TransformableBoxState>): TextModeEvent()
    data class AddTransformableTextBox(val textBoxState: TransformableTextBoxState): TextModeEvent()
    data class UpdateShouldGoToNextScreen(val shouldGoToNextScreen: Boolean): TextModeEvent()
    data class ShowTextField(val textFieldState: TextModeState.TextFieldState): TextModeEvent()
    object HideTextField: TextModeEvent()
}