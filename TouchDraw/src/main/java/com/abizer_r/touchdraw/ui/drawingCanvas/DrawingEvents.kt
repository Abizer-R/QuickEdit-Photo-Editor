package com.abizer_r.touchdraw.ui.drawingCanvas

import com.abizer_r.touchdraw.ui.drawingCanvas.models.PathDetails


sealed class DrawingEvents {
    data class AddNewPath(val pathDetail: PathDetails): DrawingEvents()
}