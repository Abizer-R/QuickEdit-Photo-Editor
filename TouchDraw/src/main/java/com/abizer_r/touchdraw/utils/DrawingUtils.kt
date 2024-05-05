package com.abizer_r.touchdraw.utils

import com.abizer_r.touchdraw.ui.drawingCanvas.DrawingState
import com.abizer_r.touchdraw.ui.drawingCanvas.models.PaintValues
import com.abizer_r.touchdraw.ui.drawingCanvas.drawingTool.DrawingTool
import com.abizer_r.touchdraw.ui.drawingCanvas.drawingTool.shapes.BrushShape
import com.abizer_r.touchdraw.ui.drawingCanvas.drawingTool.shapes.DrawingShape
import com.abizer_r.touchdraw.ui.drawingCanvas.drawingTool.shapes.LineShape
import com.abizer_r.touchdraw.ui.drawingCanvas.drawingTool.shapes.OvalShape
import com.abizer_r.touchdraw.ui.drawingCanvas.drawingTool.shapes.RectangleShape
import com.abizer_r.touchdraw.ui.drawingCanvas.drawingTool.shapes.ShapeTypes
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.BottomToolbarItems

object DrawingUtils {

    const val TOUCH_TOLERANCE = 4f

//    fun getDefaultControllerBsState(
//        colorList: ArrayList<Color>
//    ) = ControllerBSState(
//        strokeWidth = 8,
//        opacity = 100,
//        drawMode = DrawMode.BRUSH,
//        colorList = colorList,
//        selectedColorIndex = 0,
//        isUndoEnabled = false,
//        isRedoEnabled = false
//    )
//
//    fun createShape(drawMode: DrawMode): DrawingShape {
//        return when (drawMode) {
//            DrawMode.ERASER -> EraserBrushShape()
//            DrawMode.BRUSH -> BrushShape()
//            DrawMode.LINE -> LineShape()
//            DrawMode.OVAL -> OvalShape()
//            DrawMode.RECTANGLE -> RectangleShape()
//        }
//    }
}

fun DrawingState.getPaintValues() = PaintValues(
    color = strokeColor,
    width = strokeWidth.toFloat(),
    alpha = opacity / 100f
)

fun BottomToolbarItems.toDrawingTool(
    shapeType: ShapeTypes
) : DrawingTool {
    return when (this) {
        BottomToolbarItems.Color,
        BottomToolbarItems.Brush -> DrawingTool.Brush
        BottomToolbarItems.Eraser -> DrawingTool.Eraser
        BottomToolbarItems.Shape -> DrawingTool.Shape(shapeType)
    }
}

fun DrawingTool.getShape() : DrawingShape {
    return when (this) {
        is DrawingTool.Brush -> BrushShape()
        is DrawingTool.Eraser -> BrushShape(isEraser = true)
        is DrawingTool.Shape -> {
            when (shapeType) {
                ShapeTypes.LINE -> LineShape()
                ShapeTypes.OVAL -> OvalShape()
                ShapeTypes.RECTANGLE -> RectangleShape()
            }
        }
    }
}