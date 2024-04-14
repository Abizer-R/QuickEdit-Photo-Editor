package com.abizer_r.sketchdraft.util

import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import com.abizer_r.sketchdraft.ui.drawingCanvas.DrawingState
import com.abizer_r.sketchdraft.ui.drawingCanvas.PathDetails
import com.abizer_r.sketchdraft.ui.drawingCanvas.controllerBottomSheet.ControllerBSState
import com.abizer_r.sketchdraft.ui.drawingCanvas.controllerBottomSheet.StrokeMode
import java.util.Stack

object DrawingUtils {

    fun getDefaultControllerBsState(
        colorList: ArrayList<Color>
    ) = ControllerBSState(
        strokeWidth = 8,
        opacity = 100,
        strokeMode = StrokeMode.BRUSH,
        colorList = colorList,
        selectedColorIndex = 0,
        isUndoEnabled = false,
        isRedoEnabled = false
    )
}

fun DrawScope.drawDefaultPath(
    pathDetails: PathDetails
) {
    drawDefaultPath(
        path = pathDetails.path,
        strokeColor = pathDetails.color,
        strokeWidth = pathDetails.width,
        alpha = pathDetails.alpha,
        strokeMode = pathDetails.strokeMode
    )
}

fun DrawScope.drawDefaultPath(
    path: Path,
    drawingState: DrawingState
) {
    drawDefaultPath(
        path = path,
        strokeColor = drawingState.strokeColor,
        strokeWidth = drawingState.strokeWidth.toFloat(),
        alpha = drawingState.opacity / 100f,
        strokeMode = drawingState.strokeMode
    )
}

fun DrawScope.drawDefaultPath(
    path: Path,
    strokeColor: Color,
    strokeWidth: Float,
    alpha: Float,
    strokeMode: StrokeMode
) {
    drawPath(
        path = path,
        brush = SolidColor(strokeColor),
        style = Stroke(
            width = strokeWidth,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        ),
        alpha = alpha,
        blendMode = if (strokeMode == StrokeMode.ERASER) BlendMode.Clear
            else BlendMode.SrcOver
    )
}

fun DrawingState.getPathDetailsForPath(
    path: Path
) = PathDetails(
    path = path,
    width = strokeWidth.toFloat(),
    alpha = opacity / 100f,
    color = strokeColor,
    strokeMode = strokeMode
)