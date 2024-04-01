package com.abizer_r.sketchdraft.ui.drawingCanvas.controllerBottomSheet


sealed class ControllerBSEvents {
    data class OpacityChanged(val opacity: Float): ControllerBSEvents()
    data class StrokeWidthChanged(val strokeWidth: Float): ControllerBSEvents()
    data class ColorSelected(val index: Int): ControllerBSEvents()
    object Undo: ControllerBSEvents()
    object Redo: ControllerBSEvents()
}