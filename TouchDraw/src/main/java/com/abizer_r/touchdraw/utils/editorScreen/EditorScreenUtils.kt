package com.abizer_r.touchdraw.utils.editorScreen

import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.state.BottomToolbarItem

object EditorScreenUtils {

    fun getDefaultBottomToolbarItemsList(): ArrayList<BottomToolbarItem> {
        return arrayListOf(
            BottomToolbarItem.CropMode,
            BottomToolbarItem.DrawMode,
            BottomToolbarItem.TextMode,
            BottomToolbarItem.EffectsMode
        )
    }
}