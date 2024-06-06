package com.abizer_r.touchdraw.ui.drawMode.stateHandling

import androidx.compose.ui.graphics.Color
import com.abizer_r.touchdraw.ui.drawMode.drawingCanvas.models.PathDetails
import com.abizer_r.touchdraw.ui.transformableViews.TransformableViewType
import com.abizer_r.touchdraw.ui.transformableViews.base.TransformableBoxState


sealed class TextModeEvent {
    data class UpdateTextFieldValue(val textInput: String): TextModeEvent()
    data class UpdateTransformableViewsList(val list: ArrayList<TransformableViewType>): TextModeEvent()
    data class AddTransformableTextView(val view: TransformableViewType.TextTransformable): TextModeEvent()
    object ShowTextField: TextModeEvent()
    object HideTextField: TextModeEvent()
}