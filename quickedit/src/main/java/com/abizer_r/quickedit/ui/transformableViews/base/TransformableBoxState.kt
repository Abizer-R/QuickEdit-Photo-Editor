package com.abizer_r.quickedit.ui.transformableViews.base

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import com.abizer_r.quickedit.ui.textMode.bottomToolbarExtension.textFormatOptions.caseOptions.TextCaseType
import com.abizer_r.quickedit.ui.textMode.bottomToolbarExtension.textFormatOptions.styleOptions.TextStyleAttr
import com.abizer_r.quickedit.utils.textMode.FontUtils
import com.abizer_r.quickedit.utils.textMode.TextModeUtils

abstract class TransformableBoxState {
    abstract val id: String
    abstract var positionOffset: Offset
    abstract var scale: Float
    abstract var rotation: Float
    abstract var isSelected: Boolean
    abstract var innerBoxSize: Size
}

data class TransformableTextBoxState(
    override val id: String,
    override var positionOffset: Offset = Offset(0f, 0f),
    override var scale: Float = 1f,
    override var rotation: Float = 0f,
    override var isSelected: Boolean = true,
    override var innerBoxSize: Size = Size.Zero,
    var text: String,
    var textColor: Color,
    val textFont: TextUnit,
    var textAlign: TextAlign = TextModeUtils.DEFAULT_TEXT_ALIGN,
    var textStyleAttr: TextStyleAttr = TextStyleAttr(),
    var textCaseType: TextCaseType = TextCaseType.DEFAULT,
    var textFontFamily: FontFamily = FontUtils.DefaultFontFamily
) : TransformableBoxState() {

}