package com.abizer_r.touchdraw.ui.editorScreen

import android.content.res.Configuration
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.abizer_r.components.theme.SketchDraftTheme
import com.abizer_r.touchdraw.utils.CustomLayerTypeComposable
import com.abizer_r.touchdraw.ui.drawingCanvas.DrawingCanvas
import com.abizer_r.touchdraw.ui.drawingCanvas.DrawingEvents
import com.abizer_r.touchdraw.ui.drawingCanvas.DrawingState
import com.abizer_r.touchdraw.ui.drawingCanvas.drawingTool.shapes.ShapeTypes
import com.abizer_r.touchdraw.ui.drawingCanvas.models.PathDetails
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.BottomToolBar
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.state.BottomToolbarEvents
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.state.BottomToolbarItem
import com.abizer_r.touchdraw.ui.editorScreen.topToolbar.TopToolBar
import com.abizer_r.touchdraw.utils.DrawingUtils
import io.mhssn.colorpicker.ColorPickerDialog
import io.mhssn.colorpicker.ColorPickerType
import java.util.Stack

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EditorScreen() {
    val colorOnBackground = MaterialTheme.colorScheme.onBackground

    var showColorPicker by remember { mutableStateOf(false) }

    var bottomToolbarState by remember {
        mutableStateOf(
            DrawingUtils.getDefaultBottomToolbarState(
                defaultColorSelected = colorOnBackground
            )
        )
    }

    val pathDetailStack = remember { Stack<PathDetails>() }
    val redoStack = remember { Stack<PathDetails>() }
    var pathDetailStackStateTrigger by remember {
        mutableStateOf(Triple(0f, pathDetailStack, redoStack))
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
            enableUndo = pathDetailStackStateTrigger.second.isNotEmpty(),
            enableRedo = redoStack.isNotEmpty(),
            onUndo = {
                if (pathDetailStack.isNotEmpty()) {
                    redoStack.push(pathDetailStack.pop())
                }
                pathDetailStackStateTrigger = pathDetailStackStateTrigger.copy(
                    first = pathDetailStackStateTrigger.first + 1
                )
            },
            onRedo = {
                if (redoStack.isNotEmpty()) {
                    pathDetailStack.push(redoStack.pop())
                }
                pathDetailStackStateTrigger = pathDetailStackStateTrigger.copy(
                    first = pathDetailStackStateTrigger.first + 1
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
                pathDetailStack = pathDetailStack,
                selectedColor = bottomToolbarState.selectedColor,
                currentTool = bottomToolbarState.selectedItem,
                onDrawingEvent = {
                    when (it) {
                        is DrawingEvents.AddNewPath -> {
                            pathDetailStack.push(it.pathDetail)
                            redoStack.clear()
                            pathDetailStackStateTrigger = pathDetailStackStateTrigger.copy(
                                first = pathDetailStackStateTrigger.first + 1
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
            bottomToolbarState = bottomToolbarState,
            onEvent = {
                when (it) {
                    is BottomToolbarEvents.OnItemClicked -> {
                        if (it.toolbarItem is BottomToolbarItem.ColorItem) {
                            showColorPicker = true
                        } else {
                            bottomToolbarState = bottomToolbarState.copy(
                                selectedItem = it.toolbarItem
                            )
                        }
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
                bottomToolbarState = bottomToolbarState.copy(
                    selectedColor = selectedColor
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