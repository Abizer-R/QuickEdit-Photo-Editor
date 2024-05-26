package com.abizer_r.touchdraw.ui.drawMode

import androidx.compose.ui.graphics.Color
import com.abizer_r.touchdraw.ui.drawMode.drawingCanvas.models.PathDetails


sealed class DrawModeEvent {
    data class AddNewPath(val pathDetail: PathDetails): DrawModeEvent()
    data class ToggleColorPicker(val selectedColor: Color?): DrawModeEvent()
    object OnUndo: DrawModeEvent()
    object OnRedo: DrawModeEvent()
}