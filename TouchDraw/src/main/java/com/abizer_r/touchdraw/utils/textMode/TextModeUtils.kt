package com.abizer_r.touchdraw.utils.textMode

import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.abizer_r.touchdraw.ui.drawMode.drawingCanvas.drawingTool.shapes.ShapeType
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.state.BottomToolbarItem
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.state.BottomToolbarState
import com.abizer_r.touchdraw.utils.drawMode.DrawingConstants

object TextModeUtils {

    @Composable
    fun getColorsForTextField(): TextFieldColors {
        return TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent

        )
    }

    fun getDefaultBottomToolbarState(): BottomToolbarState {
        val toolbarListItems = getDefaultBottomToolbarItemsList()
        return BottomToolbarState(
            toolbarItems = toolbarListItems
        )
    }

    private fun getDefaultBottomToolbarItemsList(): ArrayList<BottomToolbarItem> {
        return arrayListOf(
            BottomToolbarItem.AddItem
        )
    }
}