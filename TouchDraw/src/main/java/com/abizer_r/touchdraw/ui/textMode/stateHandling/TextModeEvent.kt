package com.abizer_r.touchdraw.ui.textMode.stateHandling

import com.abizer_r.touchdraw.ui.transformableViews.base.TextState
import com.abizer_r.touchdraw.ui.transformableViews.base.TransformableBoxState


sealed class TextModeEvent {
    data class UpdateTextFieldValue(val textInput: String): TextModeEvent()
    data class UpdateTransformableViewsList(val list: ArrayList<TransformableBoxState>): TextModeEvent()
    data class AddTransformableTextView(val textViewState: TextState): TextModeEvent()
    data class ShowTextField(val TextFieldState: TextModeState.TextFieldState): TextModeEvent()
    object HideTextField: TextModeEvent()
}