package com.abizer_r.quickedit.ui.drawMode

import ToolbarExtensionView
import android.graphics.Bitmap
import android.view.View
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.abizer_r.components.util.ImmutableList
import com.abizer_r.components.util.defaultErrorToast
import com.abizer_r.quickedit.ui.common.AnimatedToolbarContainer
import com.abizer_r.quickedit.ui.common.bottomToolbarModifier
import com.abizer_r.quickedit.ui.common.topToolbarModifier
import com.abizer_r.quickedit.ui.drawMode.drawingCanvas.DrawingCanvas
import com.abizer_r.quickedit.ui.drawMode.drawingCanvas.drawingTool.shapes.ShapeType
import com.abizer_r.quickedit.ui.drawMode.stateHandling.DrawModeEvent
import com.abizer_r.quickedit.ui.editorScreen.bottomToolbar.BottomToolBarStatic
import com.abizer_r.quickedit.ui.editorScreen.bottomToolbar.TOOLBAR_HEIGHT_MEDIUM
import com.abizer_r.quickedit.ui.editorScreen.bottomToolbar.TOOLBAR_HEIGHT_SMALL
import com.abizer_r.quickedit.ui.editorScreen.bottomToolbar.state.BottomToolbarEvent
import com.abizer_r.quickedit.ui.editorScreen.topToolbar.TopToolBar
import com.abizer_r.quickedit.utils.drawMode.CustomLayerTypeComposable
import com.abizer_r.quickedit.utils.drawMode.DrawModeUtils
import com.abizer_r.quickedit.utils.drawMode.getOpacityOrNull
import com.abizer_r.quickedit.utils.drawMode.getShapeTypeOrNull
import com.abizer_r.quickedit.utils.drawMode.getWidthOrNull
import com.abizer_r.quickedit.utils.other.anim.AnimUtils
import com.abizer_r.quickedit.utils.other.bitmap.ImmutableBitmap
import com.smarttoolfactory.screenshot.ImageResult
import com.smarttoolfactory.screenshot.ScreenshotBox
import com.smarttoolfactory.screenshot.rememberScreenshotState
import io.mhssn.colorpicker.ColorPickerDialog
import io.mhssn.colorpicker.ColorPickerType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

    val topToolbarHeight =  TOOLBAR_HEIGHT_SMALL
    val bottomToolbarHeight = TOOLBAR_HEIGHT_MEDIUM

    var toolbarVisible by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        toolbarVisible = true
        delay(AnimUtils.TOOLBAR_EXPAND_ANIM_DURATION_FAST.toLong())
        viewModel.onBottomToolbarEvent(
            BottomToolbarEvent.OnItemClicked(bottomToolbarItems.items[1])
        )
    }

    val screenshotState = rememberScreenshotState()

    val onCloseClickedLambda = remember<() -> Unit> {{
        lifeCycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            toolbarVisible = false
            delay(AnimUtils.TOOLBAR_COLLAPSE_ANIM_DURATION_FAST.toLong())
            onBackPressed()
        }
    }}

    BackHandler {
        if (state.showBottomToolbarExtension) {
            viewModel.onEvent(DrawModeEvent.UpdateToolbarExtensionVisibility(false))
        } else {
            onCloseClickedLambda()
        }
    }

    val onDoneClickedLambda = remember<() -> Unit> {{
        viewModel.handleStateBeforeCaptureScreenshot()
        lifeCycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            delay(200)  /* Delay to update the ToolbarExtensionView Visibility in ui */
            screenshotState.capture()
        }
    }}

    val handleScreenshotResult = remember<(Bitmap) -> Unit> {{ bitmap ->
        lifeCycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            toolbarVisible = false
            delay(AnimUtils.TOOLBAR_COLLAPSE_ANIM_DURATION_FAST.toLong())
            onDoneClicked(bitmap)
        }
    }}

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
                    handleScreenshotResult(mBitmap)
                } ?: context.defaultErrorToast()
            }
        }
    }

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

        AnimatedToolbarContainer(
            toolbarVisible = toolbarVisible,
            modifier = topToolbarModifier(topToolbar)
        ) {
            TopToolBar(
                modifier = Modifier,
                undoEnabled = state.pathDetailStack.isNotEmpty(),
                redoEnabled = state.redoStack.isNotEmpty(),
                toolbarHeight = topToolbarHeight,
                onUndo = onUndoLambda,
                onRedo = onRedoLambda,
                onCloseClicked = onCloseClickedLambda,
                onDoneClicked = onDoneClickedLambda
            )
        }

        val bitmap = immutableBitmap.bitmap
        val aspectRatio = remember(bitmap) {
            bitmap.width.toFloat() / bitmap.height.toFloat()
        }
        ScreenshotBox(
            modifier = Modifier
                .constrainAs(drawingView) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.wrapContent
                    height = Dimension.wrapContent
                }
                .padding(top = topToolbarHeight, bottom = bottomToolbarHeight)
                .aspectRatio(aspectRatio),
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
                    onDrawingEvent = viewModel::onEvent
                )
            }
        }

        AnimatedToolbarContainer(
            toolbarVisible = toolbarVisible,
            modifier = bottomToolbarModifier(bottomToolbar)
        ) {
            BottomToolBarStatic(
                modifier = Modifier,
                toolbarItems = bottomToolbarItems,
                showColorPickerIcon = viewModel.showColorPickerIconInToolbar,
                toolbarHeight = bottomToolbarHeight,
                selectedColor = state.selectedColor,
                selectedItem = state.selectedTool,
                onEvent = onBottomToolbarEventLambda
            )
        }


        val emptyLambda = remember<() -> Unit> {{
        }}
        val onWidthChangeLambda = remember<(Float) -> Unit> {{ mWidth ->
            viewModel.onBottomToolbarEvent(
                BottomToolbarEvent.UpdateWidth(mWidth)
            )
        }}
        val onOpacityChangeLambda = remember<(Float) -> Unit> {{ mOpacity ->
            viewModel.onBottomToolbarEvent(
                BottomToolbarEvent.UpdateOpacity(mOpacity)
            )
        }}
        val onShapeTypeChangeLambda = remember<(ShapeType) -> Unit> {{ mShapeType ->
            viewModel.onBottomToolbarEvent(
                BottomToolbarEvent.UpdateShapeType(mShapeType)
            )
        }}

        AnimatedVisibility(
            visible = state.showBottomToolbarExtension,
            modifier = Modifier
                .constrainAs(bottomToolbarExtension) {
                    bottom.linkTo(bottomToolbar.top)
                    width = Dimension.matchParent
                }
                .clickable(onClick = emptyLambda), /* added clickable{} to avoid triggering touchEvent in DrawingCanvas when clicking anywhere on toolbarExtension */
            enter = AnimUtils.toolbarExtensionExpandAnim(),
            exit = AnimUtils.toolbarExtensionCollapseAnim()

        ) {
            ToolbarExtensionView(
                modifier = Modifier.fillMaxWidth(),
                showSeparationAtBottom = true,
                width = state.selectedTool.getWidthOrNull(),
                opacity = state.selectedTool.getOpacityOrNull(),
                shapeType = state.selectedTool.getShapeTypeOrNull(),
                onWidthChange = onWidthChangeLambda,
                onOpacityChange = onOpacityChangeLambda,
                onShapeTypeChange = onShapeTypeChangeLambda
            )
        }


        val onDismissRequestLambda = remember<() -> Unit> {{
            viewModel.onEvent(DrawModeEvent.ToggleColorPicker(null))
        }}
        val onPickedColorLambda = remember<(Color) -> Unit> {{ selectedColor ->
            viewModel.onEvent(DrawModeEvent.ToggleColorPicker(selectedColor))
        }}
        ColorPickerDialog(
            show = state.showColorPicker,
            type = ColorPickerType.Circle(showAlphaBar = false),
            properties = DialogProperties(),
            onDismissRequest = onDismissRequestLambda,
            onPickedColor = onPickedColorLambda
        )

    }
}