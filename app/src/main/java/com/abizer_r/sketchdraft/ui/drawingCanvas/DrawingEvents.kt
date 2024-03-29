package com.abizer_r.sketchdraft.ui.drawingCanvas


sealed class DrawingEvents {
    data class AddNewPath(val pathDetail: PathDetails): DrawingEvents()
}