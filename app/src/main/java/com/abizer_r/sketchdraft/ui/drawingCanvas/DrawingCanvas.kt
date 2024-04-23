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
import androidx.compose.ui.input.pointer.pointerInteropFilter
import com.abizer_r.sketchdraft.ui.drawingCanvas.shapes.Shape
import com.abizer_r.sketchdraft.util.DrawingUtils
import com.abizer_r.sketchdraft.util.getPaintValues

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DrawingCanvas(
    modifier: Modifier,
    drawingState: DrawingState,
    onDrawingEvent: (DrawingEvents) -> Unit
) {
    Log.e("TEST", "DrawingCanvas: drawMode = ${drawingState.drawMode}", )

    /**
     * The variables/states below are changed inside the ".pointerInteropFilter" modifier
     * And when these are changed, the draw phase is called (compose has 3 phases: composition, layout and draw)
     * SO, Recomposition isn't triggered
     */
    var currentShape: Shape? = null
    var drawPathAction by remember { mutableStateOf<Any?>(null) }

    Canvas(
        modifier = modifier
            .pointerInteropFilter {
                when (it.action) {
                    MotionEvent.ACTION_DOWN -> {
                        currentShape = DrawingUtils.createShape(drawingState.drawMode)
                        currentShape?.initShape(startX = it.x, startY = it.y)
                    }

                    MotionEvent.ACTION_MOVE -> {
                        currentShape?.moveShape(endX = it.x, endY = it.y)


                        // Below state is just to trigger the draw() phase of canvas
                        // Without it, current path won't be drawn until the "drawState" is changed
                        drawPathAction = Offset(it.x, it.y)
                    }

                    MotionEvent.ACTION_CANCEL,
                    MotionEvent.ACTION_UP -> {
                        if (currentShape != null && currentShape!!.shouldDraw()) {
                            onDrawingEvent(
                                DrawingEvents.AddNewPath(
                                    pathDetail = PathDetails(
                                        shape = currentShape!!,
                                        paintValues = drawingState.getPaintValues()
                                    )
                                )
                            )
                        }
                    }
                }
                true
            }


    ) {
        drawingState.pathDetailStack.forEach { pathDetails ->
            pathDetails.shape.draw(
                drawScope = this,
                paintValues = pathDetails.paintValues
            )
        }

        drawPathAction?.let {
            currentShape?.draw(
                drawScope = this,
                paintValues = drawingState.getPaintValues()
            )
        }
    }
}