package com.abizer_r.touchdraw.ui.drawingCanvas.drawingTool

import com.abizer_r.touchdraw.ui.drawingCanvas.drawingTool.shapes.ShapeTypes

/**
 * TODO: delete this class (this is not used in EditorScreen implementation)
 */
sealed class DrawingTool {
    object Brush : DrawingTool()

    object Eraser : DrawingTool()

    class Shape(
        val shapeType: ShapeTypes
    ) : DrawingTool()
}