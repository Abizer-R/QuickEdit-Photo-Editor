package com.abizer_r.touchdraw.ui.editorScreen

import android.content.res.Configuration
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.abizer_r.components.theme.SketchDraftTheme
import com.abizer_r.touchdraw.utils.CustomLayerTypeComposable
import com.abizer_r.touchdraw.ui.drawingCanvas.DrawingCanvas
import com.abizer_r.touchdraw.ui.drawingCanvas.DrawingEvents
import com.abizer_r.touchdraw.ui.drawingCanvas.DrawingState
import com.abizer_r.touchdraw.ui.drawingCanvas.drawingTool.DrawingTool
import com.abizer_r.touchdraw.ui.drawingCanvas.drawingTool.shapes.ShapeTypes
import com.abizer_r.touchdraw.ui.drawingCanvas.models.PathDetails
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.BottomToolBar
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.BottomToolbarItems
import com.abizer_r.touchdraw.ui.editorScreen.topToolbar.TopToolBar
import com.abizer_r.touchdraw.utils.toDrawingTool
import io.mhssn.colorpicker.ColorPickerDialog
import io.mhssn.colorpicker.ColorPickerType
import java.util.Stack

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EditorScreen() {
    val colorOnBackground = MaterialTheme.colorScheme.onBackground

    var showColorPicker by remember { mutableStateOf(false) }
    var opacity by remember { mutableStateOf(100) }
    var strokeWidth by remember { mutableStateOf(12) }
    var shapeType by remember { mutableStateOf(ShapeTypes.LINE) }
    var selectedToolType: BottomToolbarItems by remember {
        mutableStateOf(BottomToolbarItems.Brush)
    }
    /**
     * TODO: implement selecting strokeWidth, strokeColor and opacity
     */
    var drawingState by remember {
        mutableStateOf(
            DrawingState(
                strokeWidth = strokeWidth,
                opacity = opacity,
                drawingTool = selectedToolType.toDrawingTool(shapeType = shapeType),
                strokeColor = colorOnBackground,
                pathDetailStack = Stack(),
                redoStack = Stack()
            )
        )
    }



    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val (topToolbar, bottomToolbar, drawingCanvas) = createRefs()

        TopToolBar(
            modifier = Modifier.constrainAs(topToolbar) {
                top.linkTo(parent.top)
                width = Dimension.matchParent
                height = Dimension.wrapContent
            },
            enableUndo = drawingState.pathDetailStack.isNotEmpty(),
            enableRedo = drawingState.redoStack.isNotEmpty(),
            onUndo = {
                // creating new Stack, otherwise recomposition won't get triggered
                val mPathStack = Stack<PathDetails>()
                mPathStack.addAll(drawingState.pathDetailStack)
                val mRedoStack = drawingState.redoStack
                if (mPathStack.isNotEmpty()) {
                    mRedoStack.push(mPathStack.pop())
                }
                drawingState = drawingState.copy(
                    pathDetailStack = mPathStack,
                    redoStack = mRedoStack
                )
            },
            onRedo = {
                // creating new Stack, otherwise recomposition won't get triggered
                val mPathStack = Stack<PathDetails>()
                mPathStack.addAll(drawingState.pathDetailStack)
                val mRedoStack = drawingState.redoStack
                if (mRedoStack.isNotEmpty()) {
                    mPathStack.push(mRedoStack.pop())
                }
                drawingState = drawingState.copy(
                    pathDetailStack = mPathStack,
                    redoStack = mRedoStack
                )
            }
        )

        CustomLayerTypeComposable(
            layerType = View.LAYER_TYPE_HARDWARE,
            modifier = Modifier.constrainAs(drawingCanvas) {
                top.linkTo(topToolbar.bottom)
                bottom.linkTo(bottomToolbar.top)
                width = Dimension.matchParent
                height = Dimension.fillToConstraints
            }
        ) {
            DrawingCanvas(
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
                        }
                    }
                }
            )
        }




        BottomToolBar(
            modifier = Modifier.constrainAs(bottomToolbar) {
                bottom.linkTo(parent.bottom)
                width = Dimension.matchParent
                height = Dimension.wrapContent
            },
            selectedColor = drawingState.strokeColor,
            selectedToolbarType = selectedToolType,
            onToolItemClicked = {
                when (it) {
                    BottomToolbarItems.Color -> {
                        showColorPicker = true
                    }
                    else -> {
                        selectedToolType = it
                        drawingState = drawingState.copy(
                            drawingTool = it.toDrawingTool(shapeType = shapeType)
                        )
                    }
                }
            }
        )


        ColorPickerDialog(
            show = showColorPicker,
            type = ColorPickerType.Circle(showAlphaBar = false),
            properties = DialogProperties(),
            onDismissRequest = {
                showColorPicker = false
            },
            onPickedColor = { selectedColor ->
                showColorPicker = false
                drawingState = drawingState.copy(
                    strokeColor = selectedColor
                )
            }
        )

    }
}



@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewEditorScreen() {
    SketchDraftTheme {
        EditorScreen()
    }
}