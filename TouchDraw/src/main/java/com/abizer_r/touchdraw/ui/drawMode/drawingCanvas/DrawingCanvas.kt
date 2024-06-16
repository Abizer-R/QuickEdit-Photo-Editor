package com.abizer_r.touchdraw.ui.drawMode.drawingCanvas

import android.graphics.Bitmap
import android.graphics.Paint
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.toSize
import com.abizer_r.touchdraw.ui.drawMode.stateHandling.DrawModeEvent
import com.abizer_r.touchdraw.ui.drawMode.drawingCanvas.drawingTool.shapes.AbstractShape
import com.abizer_r.touchdraw.ui.drawMode.drawingCanvas.models.PathDetails
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.state.BottomToolbarItem
import com.abizer_r.touchdraw.utils.drawMode.getShape
import java.util.Stack
import kotlin.math.roundToInt

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DrawingCanvas(
    modifier: Modifier = Modifier,
    bitmap: Bitmap? = null,
    pathDetailStack: Stack<PathDetails>,
    selectedColor: Color,
    currentTool: BottomToolbarItem,
    onDrawingEvent: (DrawModeEvent) -> Unit
) {
//    Log.e("TEST", "DrawingCanvas: drawingTool = ${drawingState.drawingTool}", )

    /**
     * The variables/states below are changed inside the ".pointerInteropFilter" modifier
     * And when these are changed, the draw phase is called (compose has 3 phases: composition, layout and draw)
     * SO, Recomposition isn't triggered
     */
    var currentShape: AbstractShape? = null
    var drawPathAction by remember { mutableStateOf<Any?>(null) }
    var previousY: Float? = remember { null }

    var bitmapReqSize by remember {
        mutableStateOf(
            bitmap?.let {
                IntSize(bitmap.width, bitmap.height)
            } ?: IntSize.Zero
        )
    }

    Canvas(
        modifier = modifier
            .pointerInteropFilter {
                when (it.action) {
                    MotionEvent.ACTION_DOWN -> {
                        currentShape = currentTool.getShape(selectedColor = selectedColor)
                        currentShape?.initShape(startX = it.x, startY = it.y)
                    }

                    MotionEvent.ACTION_MOVE -> {
                        currentShape?.moveShape(endX = it.x, endY = it.y)


                        // Below state is just to trigger the draw() phase of canvas
                        // Without it, current path won't be drawn until the "drawState" is changed
                        drawPathAction = Offset(it.x, it.y)


                        val delta = previousY?.run { it.y - previousY!! } ?: 0f
                        Log.d(
                            "TEST_DRAG",
                            "y = ${it.y.roundToInt()} | \uD835\uDEE5 ${delta.roundToInt()} | ${it.yPrecision.roundToInt()}"
                        )
                        previousY = it.y
                    }

                    MotionEvent.ACTION_CANCEL,
                    MotionEvent.ACTION_UP -> {
                        if (currentShape != null && currentShape!!.shouldDraw()) {
                            onDrawingEvent(
                                DrawModeEvent.AddNewPath(
                                    pathDetail = PathDetails(
                                        drawingShape = currentShape!!,
                                    )
                                )
                            )
                        }
                    }
                }
                true
            }


    ) {
        pathDetailStack.forEach { pathDetails ->
            Log.e("TEST", "DrawingCanvas: drawing from stack. drawingShape = ${pathDetails.drawingShape}", )
            pathDetails.drawingShape.draw(
                drawScope = this,
            )
        }

        Log.e("TEST", "DrawingCanvas: done \n\n\n", )

        drawPathAction?.let {
            currentShape?.draw(
                drawScope = this,
            )
        }
    }
}