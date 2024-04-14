package com.abizer_r.sketchdraft.ui.drawingCanvas

import android.util.Log
import android.view.MotionEvent
import android.view.View
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
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.viewinterop.AndroidView
import com.abizer_r.sketchdraft.util.drawDefaultPath
import com.abizer_r.sketchdraft.util.getPathDetailsForPath

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DrawingCanvas(
    modifier: Modifier,
    drawingState: DrawingState,
    onDrawingEvent: (DrawingEvents) -> Unit
) {
    Log.e("TEST", "DrawingCanvas: strokeMode = ${drawingState.strokeMode}", )
    var path = Path()
    var drawPathAction by remember { mutableStateOf<Any?>(null) }
    Canvas(
        modifier = modifier
            .pointerInteropFilter {
                when (it.action) {
                    MotionEvent.ACTION_DOWN -> {
                        path.moveTo(it.x, it.y)
                    }

                    MotionEvent.ACTION_MOVE -> {
                        path.lineTo(it.x, it.y)
                        // Below state is just to trigger recomposition
                        // Without it, current path won't be drawn until the "drawState" is changed
                        drawPathAction = Offset(it.x, it.y)
                    }

                    MotionEvent.ACTION_CANCEL,
                    MotionEvent.ACTION_UP -> {
                        onDrawingEvent(
                            DrawingEvents.AddNewPath(
                                drawingState.getPathDetailsForPath(path)
                            )
                        )
                        path = Path()
                    }
                }
                true
            }


    ) {
        drawingState.pathDetailStack.forEach { pathDetails ->
            drawDefaultPath(
                pathDetails = pathDetails
            )
        }

        drawPathAction?.let {
            drawDefaultPath(
                path = path,
                drawingState = drawingState
            )
        }
    }
}