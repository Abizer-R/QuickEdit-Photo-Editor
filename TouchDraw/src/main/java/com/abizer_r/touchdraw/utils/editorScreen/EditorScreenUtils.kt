package com.abizer_r.touchdraw.utils.editorScreen

import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.abizer_r.touchdraw.ui.drawMode.drawingCanvas.drawingTool.shapes.ShapeType
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.state.BottomToolbarItem
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.state.BottomToolbarState
import com.abizer_r.touchdraw.ui.transformableViews.TransformableTextBox
import com.abizer_r.touchdraw.ui.transformableViews.base.TransformableBoxEvents
import com.abizer_r.touchdraw.ui.transformableViews.base.TransformableBoxState
import com.abizer_r.touchdraw.ui.transformableViews.base.TransformableTextBoxState
import com.abizer_r.touchdraw.utils.drawMode.DrawingConstants

object EditorScreenUtils {

    fun getDefaultBottomToolbarState(): BottomToolbarState {
        val toolbarListItems = getDefaultBottomToolbarItemsList()
        return BottomToolbarState(
            toolbarItems = toolbarListItems
        )
    }

    private fun getDefaultBottomToolbarItemsList(): ArrayList<BottomToolbarItem> {
        return arrayListOf(
            BottomToolbarItem.DrawMode,
            BottomToolbarItem.TextMode
        )
    }
}