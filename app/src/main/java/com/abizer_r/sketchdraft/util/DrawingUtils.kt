package com.abizer_r.sketchdraft.util

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.abizer_r.sketchdraft.ui.drawingCanvas.DrawingState
import com.abizer_r.sketchdraft.ui.drawingCanvas.PathDetails
import com.abizer_r.sketchdraft.ui.drawingCanvas.controllerBottomSheet.ControllerBSState
import com.abizer_r.sketchdraft.ui.drawingCanvas.controllerBottomSheet.StrokeMode
import kotlin.math.abs

object DrawingUtils {

    const val TOUCH_TOLERANCE = 4f

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


fun DrawScope.drawDefaultShape(
    startOffset: Offset,
    endOffset: Offset,
    strokeColor: Color,
    strokeWidth: Float,
    alpha: Float,
    strokeMode: StrokeMode
) {
    when (strokeMode) {
        StrokeMode.LINE -> {
            drawLine(
                start = startOffset,
                end = endOffset,
                brush = SolidColor(strokeColor),
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round,
                alpha = alpha
            )
        }
        StrokeMode.OVAL -> {
            drawOval(
                topLeft = startOffset,
                size = Size(
                    width = (endOffset.x - startOffset.x),
                    height = (endOffset.y - startOffset.y)
                ),
                brush = SolidColor(strokeColor),
                style = Stroke(
                    width = strokeWidth,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                ),
                alpha = alpha
            )
        }
        StrokeMode.RECTANGLE -> {
            drawRect(
                topLeft = startOffset,
                size = Size(
                    width = (endOffset.x - startOffset.x),
                    height = (endOffset.y - startOffset.y)
                ),
                brush = SolidColor(strokeColor),
                style = Stroke(
                    width = strokeWidth,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                ),
                alpha = alpha
            )
        }
        StrokeMode.ERASER,
        StrokeMode.BRUSH ->  {
            // nothing for these 2 in this function
        }
    }
}

fun DrawingState.getPathDetailsForPath(
    path: Path,
    startOffset: Offset,
    endOffset: Offset
) = PathDetails(
    path = path,
    startOffset = startOffset,
    endOffset = endOffset,
    width = strokeWidth.toFloat(),
    alpha = opacity / 100f,
    color = strokeColor,
    strokeMode = strokeMode
)