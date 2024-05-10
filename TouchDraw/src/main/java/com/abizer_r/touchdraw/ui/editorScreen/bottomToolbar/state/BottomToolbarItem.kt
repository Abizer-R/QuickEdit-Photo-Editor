package com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.state

import androidx.compose.ui.graphics.Color
import com.abizer_r.touchdraw.ui.drawingCanvas.drawingTool.shapes.ShapeTypes

sealed class BottomToolbarItem {
    object ColorItem : BottomToolbarItem()
    class BrushTool(var width: Float, var opacity: Float) : BottomToolbarItem()
    class ShapeTool(var width: Float, var opacity: Float, var shapeType: ShapeTypes) : BottomToolbarItem()
    class EraserTool(var width: Float) : BottomToolbarItem()


    class TextTool() : BottomToolbarItem()
}