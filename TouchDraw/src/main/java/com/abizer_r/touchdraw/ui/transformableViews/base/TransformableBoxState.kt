package com.abizer_r.touchdraw.ui.transformableViews.base

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit

abstract class TransformableBoxState {
    abstract val id: String
    abstract var positionOffset: Offset
    abstract var scale: Float
    abstract var rotation: Float
    abstract var isSelected: Boolean
}

data class TransformableTextBoxState(
    override val id: String,
    override var positionOffset: Offset = Offset(0f, 0f),
    override var scale: Float = 1f,
    override var rotation: Float = 0f,
    override var isSelected: Boolean = true,
    var text: String,
    var textAlign: TextAlign,
    var textColor: Color,
    val textFont: TextUnit
) : TransformableBoxState() {

}