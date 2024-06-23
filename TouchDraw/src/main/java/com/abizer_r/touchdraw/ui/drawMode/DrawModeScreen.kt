package com.abizer_r.touchdraw.ui.drawMode

import ToolbarExtensionView
import android.graphics.Bitmap
import android.util.Log
import android.view.View
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.abizer_r.components.util.ImmutableList
import com.abizer_r.components.util.defaultErrorToast
import com.abizer_r.touchdraw.ui.drawMode.drawingCanvas.DrawingCanvas
import com.abizer_r.touchdraw.ui.drawMode.stateHandling.DrawModeEvent
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.BottomToolBar
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.state.BottomToolbarEvent
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.state.BottomToolbarItem
import com.abizer_r.touchdraw.ui.editorScreen.topToolbar.TopToolBar
import com.abizer_r.touchdraw.ui.textMode.TextModeEvent
import com.abizer_r.touchdraw.ui.textMode.getSelectedColor
import com.abizer_r.touchdraw.ui.transformableViews.base.TransformableTextBoxState
import com.abizer_r.touchdraw.utils.drawMode.CustomLayerTypeComposable
import com.abizer_r.touchdraw.utils.drawMode.DrawModeUtils
import com.abizer_r.touchdraw.utils.drawMode.getOpacityOrNull
import com.abizer_r.touchdraw.utils.drawMode.getShapeTypeOrNull
import com.abizer_r.touchdraw.utils.drawMode.getWidthOrNull
import com.abizer_r.touchdraw.utils.other.bitmap.ImmutableBitmap
import com.smarttoolfactory.screenshot.ImageResult
import com.smarttoolfactory.screenshot.ScreenshotBox
import com.smarttoolfactory.screenshot.rememberScreenshotState
import io.mhssn.colorpicker.ColorPickerDialog
import io.mhssn.colorpicker.ColorPickerType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DrawModeScreen(
    immutableBitmap: ImmutableBitmap,
    onDoneClicked: (bitmap: Bitmap) -> Unit,
    onBackPressed: () -> Unit
) {

    val context = LocalContext.current
    val lifeCycleOwner = LocalLifecycleOwner.current

    val viewModel: DrawModeViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle(
        lifecycleOwner = lifeCycleOwner
    )

    val colorOnBackground = MaterialTheme.colorScheme.onBackground
    val backgroundColor = MaterialTheme.colorScheme.background

    val bottomToolbarItems = remember {
        ImmutableList(DrawModeUtils.getDefaultBottomToolbarItemsList())
    }
    LaunchedEffect(key1 = Unit) {
        viewModel.onBottomToolbarEvent(
            BottomToolbarEvent.OnItemClicked(bottomToolbarItems.items[1])
        )
    }

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
                    onDoneClicked(mBitmap)
                } ?: context.defaultErrorToast()
            }
        }
    }

    BackHandler {
        onBackPressed()
    }

    val onCloseClickedLambda = remember<() -> Unit> {{
        onBackPressed()
    }}

    val onDoneClickedLambda = remember<() -> Unit> {{
        viewModel.handleStateBeforeCaptureScreenshot()
        lifeCycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            delay(200)  /* Delay to update the ToolbarExtensionView Visibility in ui */
            screenshotState.capture()
        }
    }}

    val onUndoLambda = remember<() -> Unit> {{
        viewModel.onEvent(DrawModeEvent.OnUndo)
    }}
    val onRedoLambda = remember<() -> Unit> {{
        viewModel.onEvent(DrawModeEvent.OnRedo)
    }}
    val onBottomToolbarEventLambda = remember<(BottomToolbarEvent) -> Unit> {{
        viewModel.onBottomToolbarEvent(it)
    }}

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
            undoEnabled = state.pathDetailStack.isNotEmpty(),
            redoEnabled = state.redoStack.isNotEmpty(),
            onUndo = onUndoLambda,
            onRedo = onRedoLambda,
            onCloseClicked = onCloseClickedLambda,
            onDoneClicked = onDoneClickedLambda
        )

        val bitmap = immutableBitmap.bitmap
        val aspectRatio = bitmap?.let {
            bitmap.width.toFloat() / bitmap.height.toFloat()
        }
        val screenShotBoxWidth = if (aspectRatio != null) {
            Dimension.ratio(aspectRatio.toString())
        } else Dimension.fillToConstraints
        ScreenshotBox(
            modifier = Modifier.constrainAs(drawingView) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(topToolbar.bottom)
                bottom.linkTo(bottomToolbar.top)
                width = screenShotBoxWidth
                height = Dimension.fillToConstraints
            },
            screenshotState = screenshotState
        ) {

            Image(
                modifier = Modifier.fillMaxSize(),
                bitmap = bitmap.asImageBitmap(),
                contentScale = ContentScale.Fit,
                contentDescription = null
            )

            CustomLayerTypeComposable(
                layerType = View.LAYER_TYPE_HARDWARE,
                modifier = Modifier.fillMaxSize()
            ) {
                DrawingCanvas(
                    modifier = Modifier.fillMaxSize(),
                    pathDetailStack = state.pathDetailStack,
                    selectedColor = state.selectedColor,
                    currentTool = state.selectedTool,
                    onDrawingEvent = {
                        viewModel.onEvent(it)
                    }
                )
            }
        }


        BottomToolBar(
            modifier = Modifier.constrainAs(bottomToolbar) {
                bottom.linkTo(parent.bottom)
                width = Dimension.matchParent
                height = Dimension.wrapContent
            },
            toolbarItems = bottomToolbarItems,
            showColorPickerIcon = viewModel.showColorPickerIconInToolbar,
            selectedColor = state.selectedColor,
            selectedItem = state.selectedTool,
            onEvent = onBottomToolbarEventLambda
        )

        if (state.showBottomToolbarExtension) {
            ToolbarExtensionView(
                modifier = Modifier
                    .constrainAs(bottomToolbarExtension) {
                        bottom.linkTo(bottomToolbar.top)
                        width = Dimension.matchParent
                    }
                    .clickable { }, /* added clickable{} to avoid triggering touchEvent in DrawingCanvas when clicking anywhere on toolbarExtension */
                width = state.selectedTool.getWidthOrNull(),
                onWidthChange = { mWidth ->
                    viewModel.onBottomToolbarEvent(
                        BottomToolbarEvent.UpdateWidth(mWidth)
                    )
                },
                opacity = state.selectedTool.getOpacityOrNull(),
                onOpacityChange = { mOpacity ->
                    viewModel.onBottomToolbarEvent(
                        BottomToolbarEvent.UpdateOpacity(mOpacity)
                    )
                },
                shapeType = state.selectedTool.getShapeTypeOrNull(),
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