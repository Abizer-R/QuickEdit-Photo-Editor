package com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.state

import androidx.compose.ui.graphics.Color

data class BottomToolbarState(
    val toolbarItems: ArrayList<BottomToolbarItem>,
    val selectedItem: BottomToolbarItem,
    val selectedColor: Color
)