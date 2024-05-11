package com.abizer_r.touchdraw.ui.editorScreen

import ToolbarExtensionView
import android.content.res.Configuration
import android.util.Log
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.abizer_r.components.theme.SketchDraftTheme
import com.abizer_r.touchdraw.ui.drawingCanvas.DrawingCanvas
import com.abizer_r.touchdraw.ui.drawingCanvas.DrawingEvents
import com.abizer_r.touchdraw.ui.drawingCanvas.models.PathDetails
import com.abizer_r.touchdraw.ui.drawingView.DrawingView
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.BottomToolBar
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.state.BottomToolbarEvents
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.state.BottomToolbarItem
import com.abizer_r.touchdraw.ui.editorScreen.topToolbar.TopToolBar
import com.abizer_r.touchdraw.ui.textInputScreen.TextInputScreen
import com.abizer_r.touchdraw.ui.transformableViews.TransformableBox
import com.abizer_r.touchdraw.ui.transformableViews.TransformableBoxEvents
import com.abizer_r.touchdraw.ui.transformableViews.TransformableBoxState
import com.abizer_r.touchdraw.ui.transformableViews.TransformableContainerState
import com.abizer_r.touchdraw.utils.CustomLayerTypeComposable
import com.abizer_r.touchdraw.utils.DrawingUtils
import com.abizer_r.touchdraw.utils.getOpacityOrNull
import com.abizer_r.touchdraw.utils.getShapeTypeOrNull
import com.abizer_r.touchdraw.utils.getWidthOrNull
import com.abizer_r.touchdraw.utils.setOpacityIfPossible
import com.abizer_r.touchdraw.utils.setShapeTypeIfPossible
import com.abizer_r.touchdraw.utils.setWidthIfPossible
import io.mhssn.colorpicker.ColorPickerDialog
import io.mhssn.colorpicker.ColorPickerType
import java.util.Stack
import java.util.UUID

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EditorScreen() {
    val colorOnBackground = MaterialTheme.colorScheme.onBackground

    var showColorPicker by remember { mutableStateOf(false) }
    var showBottomToolbarExtension by remember { mutableStateOf(false) }
    var showTextInputScreen by remember { mutableStateOf(false) }

    var bottomToolbarState by remember {
        mutableStateOf(
            DrawingUtils.getDefaultBottomToolbarState(
                defaultColorSelected = colorOnBackground
            )
        )
    }

    var transformableContainerState by remember {
        mutableStateOf(TransformableContainerState())
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

        DrawingView(
            modifier = Modifier.constrainAs(editorLayout) {
                top.linkTo(topToolbar.bottom)
                bottom.linkTo(bottomToolbar.top)
                width = Dimension.matchParent
                height = Dimension.fillToConstraints
            },
            pathDetailStack = pathDetailStack,
            selectedColor = bottomToolbarState.selectedColor,
            currentTool = bottomToolbarState.selectedItem,
            transformableViewsList = transformableContainerState.childrenStateList,
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
            },
            onTransformViewEvent = { mEvent ->
                val stateList = transformableContainerState.childrenStateList
                when(mEvent) {
                    is TransformableBoxEvents.OnDrag -> {
                        val index = stateList.indexOfFirst { mEvent.id == it.id }
                        if (index >= 0 && index < stateList.size) {
                            stateList[index] = stateList[index].copy(
                                positionOffset = (stateList[index].positionOffset + mEvent.dragAmount)
                            )
                        }
                    }

                    is TransformableBoxEvents.OnZoom -> {
                        val index = stateList.indexOfFirst { mEvent.id == it.id }
                        if (index >= 0 && index < stateList.size) {
                            stateList[index] = stateList[index].copy(
                                scale = (stateList[index].scale * mEvent.zoomAmount).coerceIn(1f, 5f)
                            )
                        }
                    }

                    is TransformableBoxEvents.OnRotate -> {
                        val index = stateList.indexOfFirst { mEvent.id == it.id }
                        if (index >= 0 && index < stateList.size) {
                            stateList[index] = stateList[index].copy(
                                rotation = stateList[index].rotation + mEvent.rotationChange
                            )
                        }
                    }
                }

                transformableContainerState = transformableContainerState.copy(
                    childrenStateList = stateList,
                    triggerRecomposition = transformableContainerState.triggerRecomposition + 1
                )

                Log.e("TEST_event2", "childrenList = ${transformableContainerState.childrenStateList}", )
            }
        )

        if (showTextInputScreen) {
            TextInputScreen(
                modifier = Modifier
                    .constrainAs(textInputScreen) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.matchParent
                        height = Dimension.fillToConstraints
                    }
                    .zIndex(1f),
                onDoneClicked = { mText ->
                    showTextInputScreen = showTextInputScreen.not()
                    /**
                     * TODO: make a draggable text item using "mText"
                     */

                    val stateList = transformableContainerState.childrenStateList
                    stateList.add(
                        TransformableBoxState(
                            id = UUID.randomUUID().toString(),
                            positionOffset = Offset(0f, 0f),
                            viewSizeInDp = 100.dp,
                            scale = 1f,
                            rotation = 0f
                        )
                    )
                    transformableContainerState = transformableContainerState.copy(
                        childrenStateList = stateList,
                        triggerRecomposition = transformableContainerState.triggerRecomposition + 1
                    )
                    Log.e("TEST", "EditorScreen: onDoneClicked() - mText = $mText", )
                },
                onBackPressed = {
                    showTextInputScreen = false
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
                        when (it.toolbarItem) {
                            is BottomToolbarItem.ColorItem -> {
                                showColorPicker = true
                            }

                            is BottomToolbarItem.TextTool -> {
                                showTextInputScreen = showTextInputScreen.not()
                            }

                            // Clicked on already selected item
                            bottomToolbarState.selectedItem -> {
                                showBottomToolbarExtension = showBottomToolbarExtension.not()
                            }

                            // clicked on another item
                            else -> {
                                showBottomToolbarExtension = false
                                bottomToolbarState = bottomToolbarState.copy(
                                    selectedItem = it.toolbarItem
                                )
                            }
                        }
                    }
                }
            }
        )

        if (showBottomToolbarExtension) {
            ToolbarExtensionView(
                modifier = Modifier
                    .constrainAs(bottomToolbarExtension) {
                        bottom.linkTo(bottomToolbar.top)
                        width = Dimension.matchParent
                    }
                    .padding(bottom = 2.dp) /* added padding to get a visual separation between BottomToolbar and extension */
                    .clickable { }, /* added clickable{} to avoid triggering touchEvent in DrawingCanvas when clicking anywhere on toolbarExtension */
                width = bottomToolbarState.selectedItem.getWidthOrNull(),
                onWidthChange = { mWidth ->
                    bottomToolbarState = bottomToolbarState.copy(
                        selectedItem = bottomToolbarState.selectedItem.setWidthIfPossible(mWidth),
                        recompositionTriggerValue = bottomToolbarState.recompositionTriggerValue + 1
                    )
                },
                opacity = bottomToolbarState.selectedItem.getOpacityOrNull(),
                onOpacityChange = { mOpacity ->
                    bottomToolbarState = bottomToolbarState.copy(
                        selectedItem = bottomToolbarState.selectedItem.setOpacityIfPossible(mOpacity),
                        recompositionTriggerValue = bottomToolbarState.recompositionTriggerValue + 1
                    )
                },
                shapeType = bottomToolbarState.selectedItem.getShapeTypeOrNull(),
                onShapeTypeChange = { mShapeType ->
                    bottomToolbarState = bottomToolbarState.copy(
                        selectedItem = bottomToolbarState.selectedItem.setShapeTypeIfPossible(mShapeType),
                        recompositionTriggerValue = bottomToolbarState.recompositionTriggerValue + 1
                    )
                }
            )
        }


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