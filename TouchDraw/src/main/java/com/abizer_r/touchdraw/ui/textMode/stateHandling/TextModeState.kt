package com.abizer_r.touchdraw.ui.textMode.stateHandling

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.abizer_r.touchdraw.ui.transformableViews.base.TransformableBoxState

data class TextModeState(
    val textFieldState: TextFieldState = TextFieldState(),
//    val textFieldValue: String = "",
    val transformableViewStateList: ArrayList<TransformableBoxState> = arrayListOf(),
    val recompositionTrigger: Long = 0
) {
    data class TextFieldState(
        val isVisible: Boolean = true,
        val textStateId: String? = null,
        val text: String = "",
        val textAlign: TextAlign = TextAlign.Center,
        val textColor: Color = Color.White
    )
}