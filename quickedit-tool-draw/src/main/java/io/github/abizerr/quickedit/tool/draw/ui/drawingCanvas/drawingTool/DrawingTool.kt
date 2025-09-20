package io.github.abizerr.quickedit.tool.draw.ui.drawingCanvas.drawingTool

import io.github.abizerr.quickedit.tool.draw.models.shapes.ShapeType

/**
 * TODO: delete this class (this is not used in EditorScreen implementation)
 */
sealed class DrawingTool {
    object Brush : DrawingTool()

    object Eraser : DrawingTool()

    class Shape(
        val shapeType: ShapeType
    ) : DrawingTool()
}