package com.abizer_r.quickedit.utils.editorScreen

import com.abizer_r.quickedit.ui.editorScreen.bottomToolbar.state.BottomToolbarItem

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