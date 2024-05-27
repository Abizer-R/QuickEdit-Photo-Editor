package com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.state

import com.abizer_r.touchdraw.ui.drawMode.drawingCanvas.drawingTool.shapes.ShapeType

sealed class BottomToolbarItem {
    // ---------- EditorScreen Items - START
    object DrawMode : BottomToolbarItem()
    object TextMode : BottomToolbarItem()
    // ---------- EditorScreen Items - End


    // ---------- DrawModeScreen Items - START
    object ColorItem : BottomToolbarItem()
    class BrushTool(var width: Float, var opacity: Float) : BottomToolbarItem()
    class ShapeTool(var width: Float, var opacity: Float, var shapeType: ShapeType) : BottomToolbarItem()
    class EraserTool(var width: Float) : BottomToolbarItem()
    // ---------- DrawModeScreen Items - End


    // ---------- TextModeScreen Items - START
    object AddItem : BottomToolbarItem()
    // ---------- TextModeScreen Items - End


    object NONE : BottomToolbarItem()
}