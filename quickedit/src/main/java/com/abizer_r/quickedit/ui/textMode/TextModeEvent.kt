package com.abizer_r.quickedit.ui.textMode

import androidx.compose.ui.unit.TextUnit
import com.abizer_r.quickedit.ui.textMode.textEditorLayout.TextEditorState
import com.abizer_r.quickedit.ui.transformableViews.base.TransformableTextBoxState
import com.abizer_r.quickedit.ui.transformableViews.base.TransformableBoxState


sealed class TextModeEvent {
    data class UpdateTransformableViewsList(val list: ArrayList<TransformableBoxState>): TextModeEvent()
    data class AddTransformableTextBox(val textBoxState: TransformableTextBoxState): TextModeEvent()
    data class ShowTextEditor(val textEditorState: TextEditorState? = null): TextModeEvent()
    data class UpdateToolbarExtensionVisibility(val isVisible: Boolean): TextModeEvent()
    object HideTextEditor: TextModeEvent()
}