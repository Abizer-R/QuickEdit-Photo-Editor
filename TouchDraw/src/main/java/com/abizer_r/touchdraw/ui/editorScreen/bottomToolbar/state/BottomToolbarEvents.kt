package com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.state

sealed class BottomToolbarEvents {
    data class OnItemClicked(val toolbarItem: BottomToolbarItem): BottomToolbarEvents()
}