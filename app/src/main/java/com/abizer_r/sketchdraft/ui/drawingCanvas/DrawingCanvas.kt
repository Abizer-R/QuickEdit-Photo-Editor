package com.abizer_r.sketchdraft.ui.drawingCanvas

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
import com.abizer_r.sketchdraft.util.drawDefaultPath
import com.abizer_r.sketchdraft.util.getPathDetailsForPath

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DrawingCanvas(
    modifier: Modifier,
    drawingState: DrawingState,
    onDrawingEvent: (DrawingEvents) -> Unit
) {
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
        drawingState.pathDetailList.forEach { pathDetails ->
            drawDefaultPath(
                path = pathDetails.path,
                strokeColor = pathDetails.color,
                strokeWidth = pathDetails.width,
                alpha = pathDetails.alpha
            )
        }

        drawPathAction?.let {
            drawDefaultPath(
                path = path,
                strokeColor = drawingState.strokeColor,
                strokeWidth = drawingState.strokeWidth.toFloat(),
                alpha = drawingState.opacity / 100f
            )
        }
    }
}