package com.abizer_r.sketchdraft.ui.drawingCanvas

import android.util.Log
import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.input.pointer.pointerInteropFilter
import com.abizer_r.sketchdraft.ui.drawingCanvas.controllerBottomSheet.StrokeMode
import com.abizer_r.sketchdraft.util.drawDefaultPath
import com.abizer_r.sketchdraft.util.drawDefaultShape
import com.abizer_r.sketchdraft.util.getPathDetailsForPath

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DrawingCanvas(
    modifier: Modifier,
    drawingState: DrawingState,
    onDrawingEvent: (DrawingEvents) -> Unit
) {
    Log.e("TEST", "DrawingCanvas: strokeMode = ${drawingState.strokeMode}", )

    /**
     * The variables/states below are changed inside the ".pointerInteropFilter" modifier
     * And when these are changed, the draw phase is called (compose has 3 phases: composition, layout and draw)
     * SO, Recomposition isn't triggered
     */
    var path = Path()
    var drawPathAction by remember { mutableStateOf<Any?>(null) }
    var startOffSet = Offset.Unspecified
    var endOffset = Offset.Unspecified
//    var left = -1f
//    var top = -1f
//    var right = -1f
//    var bottom = -1f

    Canvas(
        modifier = modifier
            .pointerInteropFilter {
                when (it.action) {
                    MotionEvent.ACTION_DOWN -> {
                        when (drawingState.strokeMode) {
                            StrokeMode.ERASER, StrokeMode.BRUSH -> {
                                path.moveTo(it.x, it.y)
                            }

                            StrokeMode.LINE, StrokeMode.OVAL,
                            StrokeMode.RECTANGLE -> {
                                startOffSet = Offset(it.x, it.y)
                            }
                        }
                    }

                    MotionEvent.ACTION_MOVE -> {
                        when (drawingState.strokeMode) {
                            StrokeMode.ERASER, StrokeMode.BRUSH -> {
                                path.lineTo(it.x, it.y)
                            }

                            StrokeMode.LINE, StrokeMode.OVAL,
                            StrokeMode.RECTANGLE -> {
                                endOffset = Offset(it.x, it.y)
                            }
                        }
                        // Below state is just to trigger recomposition
                        // Without it, current path won't be drawn until the "drawState" is changed
                        drawPathAction = Offset(it.x, it.y)
                    }

                    MotionEvent.ACTION_CANCEL,
                    MotionEvent.ACTION_UP -> {
                        if (
                            drawingState.strokeMode == StrokeMode.ERASER || drawingState.strokeMode == StrokeMode.BRUSH ||
                            (startOffSet != Offset.Unspecified && endOffset != Offset.Unspecified)
                        ) {
                            onDrawingEvent(
                                DrawingEvents.AddNewPath(
                                    drawingState.getPathDetailsForPath(path, startOffSet, endOffset)
                                )
                            )
                        }
                        path = Path()
                    }
                }
                true
            }


    ) {
        drawingState.pathDetailStack.forEach { pathDetails ->
            if (pathDetails.strokeMode == StrokeMode.BRUSH || pathDetails.strokeMode == StrokeMode.ERASER) {
                drawDefaultPath(
                    pathDetails = pathDetails
                )
            } else {
                Log.e(
                    "TEST",
                    "DrawingCanvas: shape mode [1]:: startOffset = ${pathDetails.startOffset}, endOffset = ${pathDetails.endOffset}"
                )
                drawDefaultShape(
                    startOffset = pathDetails.startOffset,
                    endOffset = pathDetails.endOffset,
                    strokeColor = pathDetails.color,
                    strokeWidth = pathDetails.width,
                    alpha = pathDetails.alpha,
                    strokeMode = pathDetails.strokeMode
                )
            }
        }

        drawPathAction?.let {
            if (drawingState.strokeMode == StrokeMode.BRUSH || drawingState.strokeMode == StrokeMode.ERASER) {
                drawDefaultPath(
                    path = path,
                    drawingState = drawingState
                )
            } else {
                if (startOffSet != Offset.Unspecified && endOffset != Offset.Unspecified) {
                    drawDefaultShape(
                        startOffset = startOffSet,
                        endOffset = endOffset,
                        strokeColor = drawingState.strokeColor,
                        strokeWidth = drawingState.strokeWidth.toFloat(),
                        alpha = drawingState.opacity / 100f,
                        strokeMode = drawingState.strokeMode
                    )
                }
            }
        }
    }
}