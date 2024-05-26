package com.abizer_r.touchdraw.ui.drawMode

import ToolbarExtensionView
import android.graphics.Bitmap
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.abizer_r.components.util.defaultErrorToast
import com.abizer_r.touchdraw.ui.drawMode.drawingCanvas.DrawingCanvas
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.BottomToolBar
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.state.BottomToolbarEvent
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.state.BottomToolbarItem
import com.abizer_r.touchdraw.ui.editorScreen.topToolbar.TopToolBar
import com.abizer_r.touchdraw.utils.CustomLayerTypeComposable
import com.abizer_r.touchdraw.utils.getOpacityOrNull
import com.abizer_r.touchdraw.utils.getShapeTypeOrNull
import com.abizer_r.touchdraw.utils.getWidthOrNull
import com.smarttoolfactory.screenshot.ImageResult
import com.smarttoolfactory.screenshot.ScreenshotBox
import com.smarttoolfactory.screenshot.rememberScreenshotState
import io.mhssn.colorpicker.ColorPickerDialog
import io.mhssn.colorpicker.ColorPickerType
import com.abizer_r.components.R as RComponents

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DrawModeScreen(
    goToTextModeScreen: (bitmap: Bitmap) -> Unit
) {

    val context = LocalContext.current
    val lifeCycleOwner = LocalLifecycleOwner.current

    val viewModel: DrawModeViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle(
        lifecycleOwner = lifeCycleOwner
    )
    val bottomToolbarState by viewModel.bottomToolbarState.collectAsStateWithLifecycle(
        lifecycleOwner = lifeCycleOwner
    )

    val colorOnBackground = MaterialTheme.colorScheme.onBackground
    val backgroundColor = MaterialTheme.colorScheme.background

    val screenshotState = rememberScreenshotState()

    when (screenshotState.imageState.value) {
        ImageResult.Initial -> {}
        is ImageResult.Error -> {
            viewModel.shouldGoToNextScreen = false
            context.defaultErrorToast()
        }
        is ImageResult.Success -> {
            if (viewModel.shouldGoToNextScreen) {
                viewModel.shouldGoToNextScreen = false
                screenshotState.bitmap?.let { mBitmap ->
                    goToTextModeScreen(mBitmap)
                } ?: context.defaultErrorToast()
            }
        }
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        val (topToolbar, bottomToolbar, bottomToolbarExtension, drawingView) = createRefs()

        TopToolBar(
            modifier = Modifier.constrainAs(topToolbar) {
                top.linkTo(parent.top)
                width = Dimension.matchParent
                height = Dimension.wrapContent
            },
            enableUndo = state.pathDetailStack.isNotEmpty(),
            enableRedo = state.redoStack.isNotEmpty(),
            onUndo = {
                viewModel.onEvent(DrawModeEvent.OnUndo)
            },
            onRedo = {
                viewModel.onEvent(DrawModeEvent.OnRedo)
            }
        )

        ScreenshotBox(
            modifier = Modifier.constrainAs(drawingView) {
                top.linkTo(topToolbar.bottom)
                bottom.linkTo(bottomToolbar.top)
                width = Dimension.matchParent
                height = Dimension.fillToConstraints
            },
            screenshotState = screenshotState
        ) {

            CustomLayerTypeComposable(
                layerType = View.LAYER_TYPE_HARDWARE,
                modifier = Modifier.fillMaxSize()
            ) {
                DrawingCanvas(
                    pathDetailStack = state.pathDetailStack,
                    selectedColor = bottomToolbarState.selectedColor,
                    currentTool = bottomToolbarState.selectedItem,
                    onDrawingEvent = {
                        viewModel.onEvent(it)
                    }
                )
            }


//            transformableViewsList.forEach { mViewDetail ->
//                when (mViewDetail) {
//                    is TransformableViewType.TextTransformable -> {
//                        TransformableTextView(
//                            viewDetail = mViewDetail,
//                            onEvent = { mEvent ->
//                                val stateList = transformableContainerState.transformableViewsList
//                                when(mEvent) {
//                                    is TransformableBoxEvents.OnDrag -> {
//                                        val index = stateList.indexOfFirst { mEvent.id == it.getId() }
//                                        if (index >= 0 && index < stateList.size) {
//                                            stateList[index] = stateList[index].setPositionOffset(
//                                                stateList[index].getPositionOffset() + mEvent.dragAmount
//                                            )
//                                        }
//                                    }
//
//                                    is TransformableBoxEvents.OnZoom -> {
//                                        val index = stateList.indexOfFirst { mEvent.id == it.getId() }
//                                        if (index >= 0 && index < stateList.size) {
//                                            stateList[index] = stateList[index].setScale(
//                                                (stateList[index].getScale() * mEvent.zoomAmount).coerceIn(0.5f, 5f)
//                                            )
//                                        }
//                                    }
//
//                                    is TransformableBoxEvents.OnRotate -> {
//                                        val index = stateList.indexOfFirst { mEvent.id == it.getId() }
//                                        if (index >= 0 && index < stateList.size) {
//                                            stateList[index] = stateList[index].setRotation(
//                                                stateList[index].getRotation() + mEvent.rotationChange
//                                            )
//                                        }
//                                    }
//                                }
//
//                                transformableContainerState = transformableContainerState.copy(
//                                    transformableViewsList = stateList,
//                                    triggerRecomposition = transformableContainerState.triggerRecomposition + 1
//                                )
//
//                                Log.e("TEST_event2", "childrenList = ${transformableContainerState.transformableViewsList}", )
//
//                            }
//                        )
//                    }
//                }
//            }
        }


        BottomToolBar(
            modifier = Modifier.constrainAs(bottomToolbar) {
                bottom.linkTo(parent.bottom)
                width = Dimension.matchParent
                height = Dimension.wrapContent
            },
            bottomToolbarState = bottomToolbarState,
            onEvent = {
                if (it is BottomToolbarEvent.OnItemClicked && it.toolbarItem is BottomToolbarItem.TextTool) {
                    viewModel.shouldGoToNextScreen = true
                    screenshotState.capture()
                } else {
                    viewModel.onBottomToolbarEvent(it)
                }
            }
        )

        if (state.showBottomToolbarExtension) {
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
                    viewModel.onBottomToolbarEvent(
                        BottomToolbarEvent.UpdateWidth(mWidth)
                    )
                },
                opacity = bottomToolbarState.selectedItem.getOpacityOrNull(),
                onOpacityChange = { mOpacity ->
                    viewModel.onBottomToolbarEvent(
                        BottomToolbarEvent.UpdateOpacity(mOpacity)
                    )
                },
                shapeType = bottomToolbarState.selectedItem.getShapeTypeOrNull(),
                onShapeTypeChange = { mShapeType ->
                    viewModel.onBottomToolbarEvent(
                        BottomToolbarEvent.UpdateShapeType(mShapeType)
                    )
                }
            )
        }


        ColorPickerDialog(
            show = state.showColorPicker,
            type = ColorPickerType.Circle(showAlphaBar = false),
            properties = DialogProperties(),
            onDismissRequest = {
                viewModel.onEvent(DrawModeEvent.ToggleColorPicker(null))
            },
            onPickedColor = { selectedColor ->
                viewModel.onEvent(DrawModeEvent.ToggleColorPicker(selectedColor))
            }
        )

    }
}