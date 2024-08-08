package com.abizer_r.quickedit.ui.textMode

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.abizer_r.quickedit.ui.editorScreen.bottomToolbar.state.BottomToolbarItem
import com.abizer_r.quickedit.utils.ColorUtils
import com.abizer_r.quickedit.ui.transformableViews.base.TransformableBoxState

data class TextModeState(
    val transformableViewStateList: ArrayList<TransformableBoxState> = arrayListOf(),
    val selectedTool: BottomToolbarItem = BottomToolbarItem.NONE,
    val showBottomToolbarExtension: Boolean = false,
    val recompositionTrigger: Long = 0
) {

}