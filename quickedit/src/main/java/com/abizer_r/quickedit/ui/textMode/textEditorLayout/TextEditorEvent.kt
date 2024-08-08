package com.abizer_r.quickedit.ui.textMode.textEditorLayout

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import com.abizer_r.quickedit.ui.transformableViews.base.TransformableTextBoxState
import com.abizer_r.quickedit.ui.transformableViews.base.TransformableBoxState


sealed class TextEditorEvent {
    data class UpdateTextFieldValue(val textInput: String): TextEditorEvent()
    data class SelectTextColor(val index: Int, val color: Color): TextEditorEvent()
    data class SelectTextAlign(val index: Int, val textAlign: TextAlign): TextEditorEvent()
}