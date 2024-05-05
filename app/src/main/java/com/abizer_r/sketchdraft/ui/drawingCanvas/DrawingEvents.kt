package com.abizer_r.sketchdraft.ui.drawingCanvas

import com.abizer_r.touchdraw.ui.drawingCanvas.PathDetails


sealed class DrawingEvents {
    data class AddNewPath(val pathDetail: PathDetails): DrawingEvents()
}