package com.abizer_r.sketchdraft

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abizer_r.sketchdraft.ui.drawingCanvas.CustomLayerTypeComposable
import com.abizer_r.sketchdraft.ui.drawingCanvas.DrawingCanvas
import com.abizer_r.sketchdraft.ui.drawingCanvas.DrawingEvents
import com.abizer_r.sketchdraft.ui.drawingCanvas.DrawingState
import com.abizer_r.sketchdraft.ui.drawingCanvas.PathDetails
import com.abizer_r.sketchdraft.ui.drawingCanvas.controllerBottomSheet.BrushControllerBottomSheet
import com.abizer_r.sketchdraft.ui.drawingCanvas.controllerBottomSheet.ControllerBSEvents
import com.abizer_r.sketchdraft.ui.drawingCanvas.controllerBottomSheet.getSelectedColor
import com.abizer_r.sketchdraft.ui.theme.SketchDraftTheme
import com.abizer_r.sketchdraft.util.AppUtils
import com.abizer_r.sketchdraft.util.DrawingUtils
import java.util.Stack
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SketchDraftTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    var controllerBsState by remember {
        mutableStateOf(
            DrawingUtils.getDefaultControllerBsState(
                colorList = AppUtils.colorList
            )
        )
    }

    var drawingState by remember {
        mutableStateOf(
            DrawingState(
                strokeWidth = controllerBsState.strokeWidth,
                opacity = controllerBsState.opacity,
                drawMode = controllerBsState.drawMode,
                strokeColor = controllerBsState.getSelectedColor(),
                pathDetailStack = Stack(),
                redoStack = Stack()
            )
        )
    }

    val scaffoldState = rememberBottomSheetScaffoldState()
    val dragHandleIconSize = 32
    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetDragHandle = {
            Image(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .size(dragHandleIconSize.dp),
                imageVector = Icons.Outlined.KeyboardArrowUp,
                contentDescription = null,
                colorFilter = ColorFilter.tint(color = Color.White)
            )
        },
        sheetContent = {
            BrushControllerBottomSheet(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                controllerBsState = controllerBsState,
                onEvent = {
                    when (it) {
                        is ControllerBSEvents.OpacityChanged -> {
                            controllerBsState = controllerBsState.copy(
                                opacity = it.opacity.roundToInt()
                            )
                            drawingState = drawingState.copy(
                                opacity = it.opacity.roundToInt()
                            )
                        }
                        is ControllerBSEvents.StrokeWidthChanged -> {
                            controllerBsState = controllerBsState.copy(
                                strokeWidth = it.strokeWidth.roundToInt()
                            )
                            drawingState = drawingState.copy(
                                strokeWidth = it.strokeWidth.roundToInt()
                            )
                        }
                        is ControllerBSEvents.ColorSelected -> {
                            controllerBsState = controllerBsState.copy(
                                selectedColorIndex = it.index
                            )
                            drawingState = drawingState.copy(
                                strokeColor = controllerBsState.getSelectedColor()
                            )
                        }
                        is ControllerBSEvents.StrokeModeChanged -> {
                            controllerBsState = controllerBsState.copy(
                                drawMode = it.drawMode
                            )
                            drawingState = drawingState.copy(
                                drawMode = it.drawMode
                            )
                        }

                        ControllerBSEvents.Undo -> {
                            // creating new Stack, otherwise recomposition won't get triggered
                            val mPathStack = Stack<PathDetails>()
                            mPathStack.addAll(drawingState.pathDetailStack)
                            val mRedoStack = drawingState.redoStack
                            mRedoStack.push(mPathStack.pop())
                            drawingState = drawingState.copy(
                                pathDetailStack = mPathStack,
                                redoStack = mRedoStack
                            )
                            controllerBsState = controllerBsState.copy(
                                isUndoEnabled = mPathStack.isNotEmpty(),
                                isRedoEnabled = mRedoStack.isNotEmpty()
                            )
                        }
                        ControllerBSEvents.Redo -> {
                            // creating new Stack, otherwise recomposition won't get triggered
                            val mPathStack = Stack<PathDetails>()
                            mPathStack.addAll(drawingState.pathDetailStack)
                            val mRedoStack = drawingState.redoStack
                            mPathStack.push(mRedoStack.pop())
                            drawingState = drawingState.copy(
                                pathDetailStack = mPathStack,
                                redoStack = mRedoStack
                            )
                            controllerBsState = controllerBsState.copy(
                                isUndoEnabled = mPathStack.isNotEmpty(),
                                isRedoEnabled = mRedoStack.isNotEmpty()
                            )
                        }
                    }
                }
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(MaterialTheme.colorScheme.background)
        ) {
            /**
             * This is required to make eraser feature work properly
             */
            CustomLayerTypeComposable(
                layerType = View.LAYER_TYPE_HARDWARE
            ) {
                DrawingCanvas(
                    modifier = Modifier.fillMaxSize(),
                    drawingState = drawingState,
                    onDrawingEvent = { drawingEvent ->
                        when (drawingEvent) {
                            is DrawingEvents.AddNewPath -> {
                                // creating new Stack, otherwise recomposition won't get triggered
                                val mPathStack = Stack<PathDetails>()
                                mPathStack.addAll(drawingState.pathDetailStack)
                                mPathStack.push(drawingEvent.pathDetail)
                                drawingState = drawingState.copy(
                                    pathDetailStack = mPathStack,
                                    redoStack = Stack()
                                )
                                controllerBsState = controllerBsState.copy(
                                    isUndoEnabled = true,
                                    isRedoEnabled = false
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun MainScreenPreview() {

    SketchDraftTheme {
        MainScreen()
    }
}