package com.abizer_r.quickedit.ui.drawMode.stateHandling

import androidx.compose.ui.graphics.Color
import io.github.abizerr.quickedit.tool.draw.models.PathDetails


sealed class DrawModeEvent {
    data class AddNewPath(val pathDetail: PathDetails): DrawModeEvent()
    data class ToggleColorPicker(val selectedColor: Color?): DrawModeEvent()
    data class UpdateToolbarExtensionVisibility(val isVisible: Boolean): DrawModeEvent()
    object OnUndo: DrawModeEvent()
    object OnRedo: DrawModeEvent()
}