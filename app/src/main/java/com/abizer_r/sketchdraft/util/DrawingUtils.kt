package com.abizer_r.sketchdraft.util

import androidx.compose.ui.graphics.Color
import com.abizer_r.sketchdraft.ui.drawingCanvas.DrawingState
import com.abizer_r.sketchdraft.ui.drawingCanvas.PaintValues
import com.abizer_r.sketchdraft.ui.drawingCanvas.controllerBottomSheet.ControllerBSState
import com.abizer_r.sketchdraft.ui.drawingCanvas.controllerBottomSheet.DrawMode
import com.abizer_r.sketchdraft.ui.drawingCanvas.shapes.BrushShape
import com.abizer_r.sketchdraft.ui.drawingCanvas.shapes.EraserBrushShape
import com.abizer_r.sketchdraft.ui.drawingCanvas.shapes.LineShape
import com.abizer_r.sketchdraft.ui.drawingCanvas.shapes.OvalShape
import com.abizer_r.sketchdraft.ui.drawingCanvas.shapes.RectangleShape
import com.abizer_r.sketchdraft.ui.drawingCanvas.shapes.Shape

object DrawingUtils {

    const val TOUCH_TOLERANCE = 4f

    fun getDefaultControllerBsState(
        colorList: ArrayList<Color>
    ) = ControllerBSState(
        strokeWidth = 8,
        opacity = 100,
        drawMode = DrawMode.BRUSH,
        colorList = colorList,
        selectedColorIndex = 0,
        isUndoEnabled = false,
        isRedoEnabled = false
    )

    fun createShape(drawMode: DrawMode): Shape {
        return when (drawMode) {
            DrawMode.ERASER -> EraserBrushShape()
            DrawMode.BRUSH -> BrushShape()
            DrawMode.LINE -> LineShape()
            DrawMode.OVAL -> OvalShape()
            DrawMode.RECTANGLE -> RectangleShape()
        }
    }
}

fun DrawingState.getPaintValues() = PaintValues(
    color = strokeColor,
    width = strokeWidth.toFloat(),
    alpha = opacity / 100f
)