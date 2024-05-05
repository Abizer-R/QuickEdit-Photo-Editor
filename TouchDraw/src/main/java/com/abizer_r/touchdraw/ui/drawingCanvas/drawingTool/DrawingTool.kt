package com.abizer_r.touchdraw.ui.drawingCanvas.drawingTool

import androidx.compose.ui.graphics.Color
import com.abizer_r.touchdraw.ui.drawingCanvas.drawingTool.shapes.DrawingShape
import com.abizer_r.touchdraw.ui.drawingCanvas.drawingTool.shapes.BrushShape
import com.abizer_r.touchdraw.ui.drawingCanvas.drawingTool.shapes.LineShape
import com.abizer_r.touchdraw.ui.drawingCanvas.drawingTool.shapes.ShapeTypes

sealed class DrawingTool {
    object Brush : DrawingTool()

    object Eraser : DrawingTool()

    class Shape(
        val shapeType: ShapeTypes
    ) : DrawingTool()
}