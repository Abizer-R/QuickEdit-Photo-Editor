package com.abizer_r.touchdraw.ui.editorScreen

import android.content.res.Configuration
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.abizer_r.components.theme.SketchDraftTheme
import com.abizer_r.touchdraw.ui.drawMode.drawingCanvas.models.PathDetails
import com.abizer_r.touchdraw.ui.editorScreen.topToolbar.TopToolBar
import com.smarttoolfactory.screenshot.rememberScreenshotState
import java.util.Stack

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EditorScreen() {

    val colorOnBackground = MaterialTheme.colorScheme.onBackground

    /**
     * In EditorState
     */
    var showColorPicker by remember { mutableStateOf(false) }
    var showBottomToolbarExtension by remember { mutableStateOf(false) }
    val pathDetailStack = remember { Stack<PathDetails>() }
    val redoStack = remember { Stack<PathDetails>() }
    var pathDetailStackStateTrigger by remember {
        mutableStateOf(Triple(0f, pathDetailStack, redoStack))
    }

//    var isTextToolSelected by remember { mutableStateOf(false) }





    val screenshotState = rememberScreenshotState()
    val screenShotImageResult by remember {
        screenshotState.imageState
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val (topToolbar, bottomToolbar, bottomToolbarExtension, editorLayout, textInputScreen) = createRefs()

        TopToolBar(
            modifier = Modifier.constrainAs(topToolbar) {
                top.linkTo(parent.top)
                width = Dimension.matchParent
                height = Dimension.wrapContent
            },
            enableUndo = pathDetailStackStateTrigger.second.isNotEmpty(),
            enableRedo = redoStack.isNotEmpty(),
            onUndo = {
                /**
                 * TODO: include the transformable views in undo-redo as well
                 */
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

//        ScreenshotBox(screenshotState = screenshotState) {
//            DrawModeScreen(
//                modifier = Modifier.constrainAs(editorLayout) {
//                    top.linkTo(topToolbar.bottom)
//                    bottom.linkTo(bottomToolbar.top)
//                    width = Dimension.matchParent
//                    height = Dimension.fillToConstraints
//                },
//                pathDetailStack = pathDetailStack,
//                selectedColor = bottomToolbarState.selectedColor,
//                currentTool = bottomToolbarState.selectedItem,
//                transformableViewsList = transformableContainerState.transformableViewsList,
//                onDrawingEvent = {
//                    when (it) {
//                        is DrawModeEvent.AddNewPath -> {
//                            pathDetailStack.push(it.pathDetail)
//                            redoStack.clear()
//                            pathDetailStackStateTrigger = pathDetailStackStateTrigger.copy(
//                                first = pathDetailStackStateTrigger.first + 1
//                            )
//                        }
//                    }
//                },
//                onTransformViewEvent = { mEvent ->
//                    val stateList = transformableContainerState.transformableViewsList
//                    when(mEvent) {
//                        is TransformableBoxEvents.OnDrag -> {
//                            val index = stateList.indexOfFirst { mEvent.id == it.getId() }
//                            if (index >= 0 && index < stateList.size) {
//                                stateList[index] = stateList[index].setPositionOffset(
//                                    stateList[index].getPositionOffset() + mEvent.dragAmount
//                                )
//                            }
//                        }
//
//                        is TransformableBoxEvents.OnZoom -> {
//                            val index = stateList.indexOfFirst { mEvent.id == it.getId() }
//                            if (index >= 0 && index < stateList.size) {
//                                stateList[index] = stateList[index].setScale(
//                                    (stateList[index].getScale() * mEvent.zoomAmount).coerceIn(0.5f, 5f)
//                                )
//                            }
//                        }
//
//                        is TransformableBoxEvents.OnRotate -> {
//                            val index = stateList.indexOfFirst { mEvent.id == it.getId() }
//                            if (index >= 0 && index < stateList.size) {
//                                stateList[index] = stateList[index].setRotation(
//                                    stateList[index].getRotation() + mEvent.rotationChange
//                                )
//                            }
//                        }
//                    }
//
//                    transformableContainerState = transformableContainerState.copy(
//                        transformableViewsList = stateList,
//                        triggerRecomposition = transformableContainerState.triggerRecomposition + 1
//                    )
//
//                    Log.e("TEST_event2", "childrenList = ${transformableContainerState.transformableViewsList}", )
//                }
//            )
//
//        }


//        BottomToolBar(
//            modifier = Modifier.constrainAs(bottomToolbar) {
//                bottom.linkTo(parent.bottom)
//                width = Dimension.matchParent
//                height = Dimension.wrapContent
//            },
//            bottomToolbarState = bottomToolbarState,
//            onEvent = {
//                when (it) {
//                    is BottomToolbarEvent.OnItemClicked -> {
//                        when (it.toolbarItem) {
//                            is BottomToolbarItem.ColorItem -> {
//                                showColorPicker = true
//                            }
//
//                            is BottomToolbarItem.TextMode -> {
//                                isTextToolSelected = isTextToolSelected.not()
//                                screenshotState.capture()
//                                onTextToolClicked()
//                            }
//
//                            // Clicked on already selected item
//                            bottomToolbarState.selectedItem -> {
//                                showBottomToolbarExtension = showBottomToolbarExtension.not()
//                            }
//
//                            // clicked on another item
//                            else -> {
//                                showBottomToolbarExtension = false
//                                bottomToolbarState = bottomToolbarState.copy(
//                                    selectedItem = it.toolbarItem
//                                )
//                            }
//                        }
//                    }
//                }
//            }
//        )
//
//        if (showBottomToolbarExtension) {
//            ToolbarExtensionView(
//                modifier = Modifier
//                    .constrainAs(bottomToolbarExtension) {
//                        bottom.linkTo(bottomToolbar.top)
//                        width = Dimension.matchParent
//                    }
//                    .padding(bottom = 2.dp) /* added padding to get a visual separation between BottomToolbar and extension */
//                    .clickable { }, /* added clickable{} to avoid triggering touchEvent in DrawingCanvas when clicking anywhere on toolbarExtension */
//                width = bottomToolbarState.selectedItem.getWidthOrNull(),
//                onWidthChange = { mWidth ->
//                    bottomToolbarState = bottomToolbarState.copy(
//                        selectedItem = bottomToolbarState.selectedItem.setWidthIfPossible(mWidth),
//                        recompositionTriggerValue = bottomToolbarState.recompositionTriggerValue + 1
//                    )
//                },
//                opacity = bottomToolbarState.selectedItem.getOpacityOrNull(),
//                onOpacityChange = { mOpacity ->
//                    bottomToolbarState = bottomToolbarState.copy(
//                        selectedItem = bottomToolbarState.selectedItem.setOpacityIfPossible(mOpacity),
//                        recompositionTriggerValue = bottomToolbarState.recompositionTriggerValue + 1
//                    )
//                },
//                shapeType = bottomToolbarState.selectedItem.getShapeTypeOrNull(),
//                onShapeTypeChange = { mShapeType ->
//                    bottomToolbarState = bottomToolbarState.copy(
//                        selectedItem = bottomToolbarState.selectedItem.setShapeTypeIfPossible(mShapeType),
//                        recompositionTriggerValue = bottomToolbarState.recompositionTriggerValue + 1
//                    )
//                }
//            )
//        }
//
//
//        ColorPickerDialog(
//            show = showColorPicker,
//            type = ColorPickerType.Circle(showAlphaBar = false),
//            properties = DialogProperties(),
//            onDismissRequest = {
//                showColorPicker = false
//            },
//            onPickedColor = { selectedColor ->
//                showColorPicker = false
//                bottomToolbarState = bottomToolbarState.copy(
//                    selectedColor = selectedColor
//                )
//            }
//        )

    }
}



@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewEditorScreen() {
    SketchDraftTheme {
        EditorScreen()
    }
}