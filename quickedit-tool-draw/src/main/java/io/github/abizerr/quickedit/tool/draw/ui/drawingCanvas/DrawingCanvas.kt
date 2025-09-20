package io.github.abizerr.quickedit.tool.draw.ui.drawingCanvas

import android.util.Log
import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.TransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInteropFilter
import io.github.abizerr.quickedit.tool.draw.models.shapes.AbstractShape
import io.github.abizerr.quickedit.tool.draw.models.PathDetails
import io.github.abizerr.quickedit.tool.draw.models.DrawToolItem
import io.github.abizerr.quickedit.tool.draw.models.getShape
import io.github.abizerr.quickedit.tool.draw.ui.DrawModeEvent
import java.util.Stack

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DrawingCanvas(
    modifier: Modifier = Modifier,
    pathDetailStack: Stack<PathDetails>,
    selectedColor: Color,
    currentTool: DrawToolItem,
    scale: Float,
    onDrawingEvent: (DrawModeEvent) -> Unit,
    transformableState: TransformableState,
    offset: Offset
) {

    /**
     * The variables/states below are changed inside the ".pointerInteropFilter" modifier
     * And when these are changed, the draw phase is called (compose has 3 phases: composition, layout and draw)
     * SO, Recomposition isn't triggered
     */
    var currentShape: AbstractShape? = null
    var drawPhaseTrigger by remember { mutableDoubleStateOf(0.0) }

    var canvasModifier = modifier
        .graphicsLayer(
            scaleX = scale,
            scaleY = scale,
            translationX = offset.x,
            translationY = offset.y
        )

    if (currentTool is DrawToolItem.PanItem) {
        canvasModifier = canvasModifier
            .transformable(transformableState)

    } else {
        canvasModifier = canvasModifier
            .pointerInteropFilter {
                val adjustedX = it.x / scale
                val adjustedY = it.y / scale
                Log.i("TEST_pan", "Drag: scale = $scale, actualPos = (${it.x}, ${it.y}), adjustedPos = ($adjustedX, $adjustedY)", )

                when (it.action) {
                    MotionEvent.ACTION_DOWN -> {
                        currentShape = currentTool.getShape(
                            selectedColor = selectedColor,
                            scale = scale,
                        )
                        currentShape.initShape(startX = adjustedX, startY = adjustedY)
                    }

                    MotionEvent.ACTION_MOVE -> {
                        currentShape?.moveShape(endX = adjustedX, endY = adjustedY)
                        // Below state is just to trigger the draw() phase of canvas
                        // Without it, current path won't be drawn until the "drawState" is changed
                        drawPhaseTrigger += 0.1
                    }

                    MotionEvent.ACTION_CANCEL,
                    MotionEvent.ACTION_UP -> {
                        if (currentShape != null && currentShape.shouldDraw()) {
                            onDrawingEvent(
                                DrawModeEvent.AddNewPath(
                                    pathDetail = PathDetails(
                                        drawingShape = currentShape,
                                    )
                                )
                            )
                        }
                    }
                }
                true
            }
    }


    Canvas(
        modifier = canvasModifier.clipToBounds()
    ) {
        pathDetailStack.forEach { pathDetails ->
            Log.e("TEST", "DrawingCanvas: drawing from stack. drawingShape = ${pathDetails.drawingShape}", )
            pathDetails.drawingShape.draw(
                drawScope = this,
            )
        }

        Log.e("TEST", "DrawingCanvas: done \n\n\n", )
        if (drawPhaseTrigger > 0) {
            currentShape?.draw(drawScope = this)
        }
    }
}

