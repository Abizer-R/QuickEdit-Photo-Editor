package io.github.abizerr.quickedit.tool.draw.ui

import androidx.compose.ui.graphics.Color
import io.github.abizerr.quickedit.tool.draw.models.DrawToolItem
import io.github.abizerr.quickedit.tool.draw.models.PathDetails
import io.github.abizerr.quickedit.engine.drawspec.ShapeType

sealed class DrawModeEvent {
    data class AddNewPath(val pathDetail: PathDetails): DrawModeEvent()
    data class ToggleColorPicker(val selectedColor: Color?): DrawModeEvent()
    data class UpdateToolbarExtensionVisibility(val isVisible: Boolean): DrawModeEvent()
    object OnUndo: DrawModeEvent()
    object OnRedo: DrawModeEvent()
    data class OnToolbarItemClicked(val toolbarItem: DrawToolItem): DrawModeEvent()
    data class UpdateWidth(val newWidth: Float): DrawModeEvent()
    data class UpdateOpacity(val newOpacity: Float): DrawModeEvent()
    data class UpdateShapeType(val newShapeType: ShapeType): DrawModeEvent()
}