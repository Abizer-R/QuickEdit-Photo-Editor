package com.abizer_r.touchdraw.ui.drawMode.stateHandling

import androidx.compose.ui.graphics.Color
import com.abizer_r.touchdraw.ui.drawMode.drawingCanvas.models.PathDetails


sealed class TextModeEvent {
    data class UpdateTextFieldValue(val textInput: String): TextModeEvent()
    object ShowTextField: TextModeEvent()
    object HideTextField: TextModeEvent()
}