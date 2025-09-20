package com.abizer_r.quickedit.ui.editorScreen.bottomToolbar.state

import io.github.abizerr.quickedit.tool.draw.models.shapes.ShapeType

sealed class BottomToolbarEvent {
    data class OnItemClicked(val toolbarItem: BottomToolbarItem): BottomToolbarEvent()
    data class UpdateWidth(val newWidth: Float): BottomToolbarEvent()
    data class UpdateOpacity(val newOpacity: Float): BottomToolbarEvent()
    data class UpdateShapeType(val newShapeType: ShapeType): BottomToolbarEvent()
}