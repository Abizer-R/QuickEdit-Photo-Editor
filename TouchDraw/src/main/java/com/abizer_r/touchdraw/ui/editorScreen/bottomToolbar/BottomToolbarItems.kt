package com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar

sealed class BottomToolbarItems {
    object Color : BottomToolbarItems()
    object Brush : BottomToolbarItems()
    object Shape : BottomToolbarItems()
    object Eraser : BottomToolbarItems()
}