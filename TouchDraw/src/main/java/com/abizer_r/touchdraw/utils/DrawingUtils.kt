package com.abizer_r.touchdraw.utils

import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.BottomToolbarItems
import com.abizer_r.touchdraw.ui.drawingCanvas.shapes.BrushShape
import com.abizer_r.touchdraw.ui.drawingCanvas.shapes.EraserBrushShape
import com.abizer_r.touchdraw.ui.drawingCanvas.shapes.LineShape
import com.abizer_r.touchdraw.ui.drawingCanvas.shapes.OvalShape
import com.abizer_r.touchdraw.ui.drawingCanvas.shapes.RectangleShape
import com.abizer_r.touchdraw.ui.drawingCanvas.shapes.Shape

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
//    fun createShape(drawMode: DrawMode): Shape {
//        return when (drawMode) {
//            DrawMode.ERASER -> EraserBrushShape()
//            DrawMode.BRUSH -> BrushShape()
//            DrawMode.LINE -> LineShape()
//            DrawMode.OVAL -> OvalShape()
//            DrawMode.RECTANGLE -> RectangleShape()
//        }
//    }
}

//fun DrawingState.getPaintValues() = PaintValues(
//    color = strokeColor,
//    width = strokeWidth.toFloat(),
//    alpha = opacity / 100f
//)