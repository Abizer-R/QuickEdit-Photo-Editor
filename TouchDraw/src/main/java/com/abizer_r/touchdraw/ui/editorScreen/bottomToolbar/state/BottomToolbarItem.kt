package com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.state

import androidx.compose.ui.graphics.Color
import com.abizer_r.touchdraw.ui.drawingCanvas.drawingTool.shapes.ShapeTypes

sealed class BottomToolbarItem {
    class ColorItem(val currentColor: Color) : BottomToolbarItem()
    class BrushTool(val width: Int, val opacity: Int) : BottomToolbarItem()
    class ShapeTool(val width: Int, val opacity: Int, val shapeTypes: ShapeTypes) : BottomToolbarItem()
    class EraserTool(val width: Int) : BottomToolbarItem()
}