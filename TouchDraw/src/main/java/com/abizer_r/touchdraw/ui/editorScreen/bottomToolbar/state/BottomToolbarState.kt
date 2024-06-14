package com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.state

import androidx.compose.ui.graphics.Color

data class BottomToolbarState(
    val toolbarItems: ArrayList<BottomToolbarItem> = arrayListOf(),
    val selectedItem: BottomToolbarItem = BottomToolbarItem.NONE,
    val selectedColor: Color = Color.White,
    val showColorPickerIcon: Boolean = true,
    val recompositionTriggerValue: Int = 0
)