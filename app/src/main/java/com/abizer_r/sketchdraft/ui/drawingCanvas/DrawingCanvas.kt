package com.abizer_r.sketchdraft.ui.drawingCanvas

import android.util.Log
import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import com.abizer_r.sketchdraft.util.drawDefaultPath
import kotlin.math.pow
import kotlin.math.sqrt

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DrawingCanvas(
    modifier: Modifier,
    strokeWidth: Float,
    strokeOpacity: Float,
    strokeColor: Color = Color.Blue,
    pathList: List<PathDetails>,
    addPathToList: (PathDetails) -> Unit
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
                        drawPathAction = Offset(it.x, it.y) // this is just to trigger recomposition
                    }

                    MotionEvent.ACTION_CANCEL,
                    MotionEvent.ACTION_UP -> {
                        addPathToList(
                            PathDetails(path, strokeWidth, strokeOpacity / 100f, strokeColor)
                        )
                        path = Path()
                    }
                }
                true
            }


    ) {
        pathList.forEach { pathDetails ->
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
                strokeColor = strokeColor,
                strokeWidth = strokeWidth,
                alpha = strokeOpacity / 100f
            )
        }
    }
}