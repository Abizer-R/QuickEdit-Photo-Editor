package com.abizer_r.quickedit.ui.drawMode

import android.graphics.Bitmap
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.outlined.Brush
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Crop
import androidx.compose.material.icons.outlined.PanTool
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.abizer_r.quickedit.R
import com.abizer_r.quickedit.ui.drawMode.bottomToolbarExtension.DrawModeToolbarExtension
import com.abizer_r.quickedit.ui.drawMode.toptoolbar.DrawModeTopToolBar
import com.abizer_r.quickedit.ui.editorScreen.bottomToolbar.TOOLBAR_HEIGHT_MEDIUM
import com.abizer_r.quickedit.ui.editorScreen.bottomToolbar.TOOLBAR_HEIGHT_SMALL
import com.abizer_r.quickedit.ui.editorScreen.bottomToolbar.state.BottomToolbarEvent
import com.abizer_r.quickedit.utils.AppUtils
import com.abizer_r.quickedit.utils.ImmutableList
import com.abizer_r.quickedit.utils.drawMode.DrawModeUtils
import com.abizer_r.quickedit.utils.drawMode.toPx
import com.abizer_r.quickedit.utils.other.bitmap.ImmutableBitmap
import com.smarttoolfactory.screenshot.ImageResult
import com.smarttoolfactory.screenshot.ScreenshotBox
import com.smarttoolfactory.screenshot.rememberScreenshotState
import io.github.abizerr.quickedit.tool.draw.models.DrawToolItem
import io.github.abizerr.quickedit.tool.draw.models.getOpacityOrNull
import io.github.abizerr.quickedit.tool.draw.models.getShapeTypeOrNull
import io.github.abizerr.quickedit.tool.draw.models.getWidthOrNull
import io.github.abizerr.quickedit.engine.drawspec.ShapeType
import io.github.abizerr.quickedit.tool.draw.ui.DrawModeEvent
import io.github.abizerr.quickedit.ui.common.AnimatedToolbarContainer
import io.github.abizerr.quickedit.ui.common.bottomToolbarModifier
import io.github.abizerr.quickedit.ui.common.topToolbarModifier
import io.github.abizerr.quickedit.ui.theme.ToolBarBackgroundColor
import io.github.abizerr.quickedit.ui.utils.anim.AnimUtils
import io.github.abizerr.quickedit.ui.utils.defaultErrorToast
import io.github.abizerr.quickedit.ui.utils.defaultTextColor
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
        ImmutableList(DrawModeUtils.getDefaultDrawToolItemsList())
    }

    val topToolbarHeight =  TOOLBAR_HEIGHT_SMALL
    val bottomToolbarHeight = TOOLBAR_HEIGHT_MEDIUM
    val verticalToolbarPaddingPx = topToolbarHeight.toPx() + bottomToolbarHeight.toPx()

    val bitmap = immutableBitmap.bitmap
    val aspectRatio = remember(bitmap) {
        bitmap.width.toFloat() / bitmap.height.toFloat()
    }

    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    val animatedScale = animateFloatAsState(targetValue = scale)
    val animatedOffset = animateOffsetAsState(targetValue = offset)
    var animateZoomPan by remember { mutableStateOf(false) }

    val constrainedOffsetScale = remember(scale, verticalToolbarPaddingPx) {
        AppUtils.getConstrainOffsetScaleOnly(context, aspectRatio, verticalToolbarPaddingPx, scale)
    }
    val transformableState = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
        // <--- NOTE --->
        // Making the min scale less than 1f will break the constrain calculation code in the DrawingCanvas
        // The constrain calculation code allows us to hold the canvas within the screen
        scale = (scale * zoomChange).coerceIn(1f, 5f)
        val newOffset = offset + (offsetChange * scale)
        val constrainedOffset = Offset(
            x = newOffset.x.coerceIn(-1 * constrainedOffsetScale.x, constrainedOffsetScale.x),
            y = newOffset.y.coerceIn(-1 * constrainedOffsetScale.y, constrainedOffsetScale.y)
        )
        offset = constrainedOffset
        Log.d("TEST_pan", "Pan: scale = $scale, offset = $offset", )
    }

    var toolbarVisible by remember { mutableStateOf(false) }
    val showResetZoomPanBtn by remember {
        derivedStateOf { scale != 1f || offset != Offset.Zero }
    }

    LaunchedEffect(key1 = Unit) {
        toolbarVisible = true
        delay(AnimUtils.TOOLBAR_EXPAND_ANIM_DURATION_FAST.toLong())
        viewModel.onEvent(
            DrawModeEvent.OnToolbarItemClicked(
                bottomToolbarItems.items[DrawModeUtils.DEFAULT_SELECTED_INDEX]
            )
        )
    }

    val resetZoomAndPan = remember<() -> Unit> {{
        lifeCycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            animateZoomPan = true
            scale = 1f // reset zoom
            offset = Offset.Zero    // reset pan
            delay(300)
            animateZoomPan = false
        }
    }}

    val screenshotState = rememberScreenshotState()

    val onCloseClickedLambda = remember<() -> Unit> {{
        lifeCycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            resetZoomAndPan()
            if (state.showBottomToolbarExtension) {
                viewModel.onEvent(DrawModeEvent.UpdateToolbarExtensionVisibility(false))
                delay(AnimUtils.TOOLBAR_COLLAPSE_ANIM_DURATION.toLong())
            }
            toolbarVisible = false
            delay(200 + AnimUtils.TOOLBAR_COLLAPSE_ANIM_DURATION_FAST.toLong())
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
        lifeCycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            resetZoomAndPan()
            viewModel.handleStateBeforeCaptureScreenshot()
            delay(400)  /* Delay to update the ToolbarExtensionView Visibility and zoom/pan in ui */
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
    val onBottomToolbarEventLambda = remember<(DrawModeEvent) -> Unit> {{
        viewModel.onEvent(it)
    }}

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        val (topToolbar, bottomToolbar, bottomToolbarExtension, drawingView, resetZoomPanBtn) = createRefs()

        AnimatedToolbarContainer(
            toolbarVisible = toolbarVisible,
            modifier = topToolbarModifier(topToolbar).zIndex(1f)
        ) {
            DrawModeTopToolBar(
                modifier = Modifier,
                undoEnabled = state.pathDetailStack.isNotEmpty(),
                redoEnabled = state.redoStack.isNotEmpty(),
                doneEnabled = state.pathDetailStack.isNotEmpty(),
                toolbarHeight = topToolbarHeight,
                onUndo = onUndoLambda,
                onRedo = onRedoLambda,
                onCloseClicked = onCloseClickedLambda,
                onDoneClicked = onDoneClickedLambda
            )
        }

        var screenshotBoxModifier = Modifier
            .constrainAs(drawingView) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.wrapContent
                height = Dimension.wrapContent
            }
            .padding(top = topToolbarHeight, bottom = bottomToolbarHeight)


        if (viewModel.shouldGoToNextScreen) {
            // constraint the screenshot box to cover only the required area
            screenshotBoxModifier = screenshotBoxModifier.aspectRatio(aspectRatio)
        }

        ScreenshotBox(
            modifier = screenshotBoxModifier,
            screenshotState = screenshotState
        ) {

            val canvasScale = if(animateZoomPan) animatedScale.value else scale
            val canvasOffset = if(animateZoomPan) animatedOffset.value else offset

            DrawingCanvasContainer(
                state = state,
                immutableBitmap = immutableBitmap,
                scale = canvasScale,
                offset = canvasOffset,
                transformableState = transformableState,
                onDrawingEvent = viewModel::onEvent
            )
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
                selectedItem = state.selectedTool,    // comment to support latest changes
                onEvent = onBottomToolbarEventLambda
            )
        }

        AnimatedVisibility(
            visible = showResetZoomPanBtn,
            modifier = Modifier
                .constrainAs(resetZoomPanBtn) {
                    top.linkTo(topToolbar.bottom)
                    end.linkTo(parent.end)
                    width = Dimension.wrapContent
                    height = Dimension.wrapContent
                }
                .padding(8.dp),
            enter = fadeIn(),
            exit = fadeOut()

        ) {

            Image(
                modifier = Modifier
                    .clip(RoundedCornerShape(5.dp))
                    .background(Color.DarkGray)
                    .padding(8.dp)
                    .size(32.dp)
                    .clickable {
                        resetZoomAndPan()
                    },
                imageVector = ImageVector.vectorResource(id = com.abizer_r.quickedit.R.drawable.baseline_fit_screen_24),
                contentDescription = null,
                colorFilter = ColorFilter.tint(
                    color = MaterialTheme.colorScheme.onBackground
                ),
            )
        }


        val emptyLambda = remember<() -> Unit> {{
        }}
        val onWidthChangeLambda = remember<(Float) -> Unit> {{ mWidth ->
            viewModel.onEvent(
                DrawModeEvent.UpdateWidth(mWidth)
            )
        }}
        val onOpacityChangeLambda = remember<(Float) -> Unit> {{ mOpacity ->
            viewModel.onEvent(
                DrawModeEvent.UpdateOpacity(mOpacity)
            )
        }}
        val onShapeTypeChangeLambda = remember<(ShapeType) -> Unit> {{ mShapeType ->
            viewModel.onEvent(
                DrawModeEvent.UpdateShapeType(mShapeType)
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
            DrawModeToolbarExtension(
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

val TOOLBAR_HEIGHT_SMALL = 48.dp
val TOOLBAR_HEIGHT_MEDIUM = 64.dp
val TOOLBAR_HEIGHT_LARGE = 88.dp
val TOOLBAR_HEIGHT_EXTRA_LARGE = 104.dp

@Composable
private fun BottomToolBarStatic(
    modifier: Modifier,
    toolbarItems: ImmutableList<DrawToolItem>,
    toolbarHeight: Dp = TOOLBAR_HEIGHT_MEDIUM,
    selectedItem: DrawToolItem = DrawToolItem.NONE,
    showColorPickerIcon: Boolean = true,
    selectedColor: Color = Color.White,
    onEvent: (DrawModeEvent) -> Unit
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(toolbarHeight)
            .background(ToolBarBackgroundColor),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        toolbarItems.items.forEachIndexed { index, mToolbarItem ->
            ToolbarItem(
                toolbarItem = mToolbarItem,
                selectedColor = selectedColor,
                showColorPickerIcon = showColorPickerIcon,
                isSelected = mToolbarItem == selectedItem,
                onEvent = onEvent
            )
        }
    }
}


@Composable
private fun ToolbarItem(
    modifier: Modifier = Modifier,
    selectedColor: Color,
    showColorPickerIcon: Boolean,
    toolbarItem: DrawToolItem,
    isSelected: Boolean,
    onEvent: (DrawModeEvent) -> Unit
) {
    val labelTextStyle = MaterialTheme.typography.bodySmall.copy(color = defaultTextColor())

    val commonPaddingModifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)

    if (toolbarItem is DrawToolItem.ColorItem) {
        ColorToolbarItem(
            modifier = modifier.then(commonPaddingModifier),
            selectedColor = selectedColor,
            showColorPickerIcon = showColorPickerIcon,
            colorItem = toolbarItem,
            labelTextStyle = labelTextStyle,
            onEvent = onEvent
        )
        return
    }
    var columnModifier = modifier.clickable {
        onEvent(DrawModeEvent.OnToolbarItemClicked(toolbarItem))
    }
    if (isSelected) {
        columnModifier = columnModifier
            .clip(RoundedCornerShape(3.dp))
            .background(MaterialTheme.colorScheme.onBackground)
            .padding((0.5).dp)
            .clip(RoundedCornerShape(3.dp))
            .background(Color.DarkGray)
//            .padding(start = 8.dp, end = 8.dp, bottom = 4.dp)
    }
    columnModifier = columnModifier.then(commonPaddingModifier)


    val (imageVector, labelText) = when (toolbarItem) {

        is DrawToolItem.EraserTool -> Pair(
            ImageVector.vectorResource(id = R.drawable.ic_eraser),
            stringResource(id = R.string.eraser)
        )

        is DrawToolItem.ShapeTool -> Pair(
            Icons.Outlined.Category,
            stringResource(id = R.string.shape)
        )

        is DrawToolItem.BrushTool -> Pair(
            ImageVector.vectorResource(id = R.drawable.ic_stylus_note),
            stringResource(id = R.string.brush)
        )

        is DrawToolItem.PanItem -> Pair(
            Icons.Outlined.PanTool,
            stringResource(id = R.string.zoom)
        )

        // We won't reach here
        else -> Pair(
            Icons.Default.AddCircleOutline,
            ""
        )
    }


    Column(
        modifier = columnModifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        val verticalPaddingBeforeSize = if (labelText.isBlank()) 4.dp else 0.dp
        val imageSize = if (labelText.isBlank()) 32.dp else 28.dp
        Image(
            modifier = Modifier
                .padding(vertical = verticalPaddingBeforeSize)
                .size(imageSize),
            contentDescription = null,
            imageVector = imageVector,
            colorFilter = ColorFilter.tint(
                color = MaterialTheme.colorScheme.onBackground
            )
        )
        Spacer(modifier = Modifier.size(
            if (labelText.isNotBlank()) 4.dp else 0.dp
        ))

        if (labelText.isNotBlank()) {
            Text(
                style = labelTextStyle,
                text = labelText
            )
        }
    }
}

@Composable
private fun ColorToolbarItem(
    modifier: Modifier = Modifier,
    selectedColor: Color,
    showColorPickerIcon: Boolean,
    colorItem: DrawToolItem.ColorItem,
    labelTextStyle: TextStyle,
    onEvent: (DrawModeEvent) -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        if (showColorPickerIcon) {
            Image(
                bitmap = ImageBitmap.imageResource(id = R.drawable.ic_color_picker),
                contentDescription = null,
                modifier = Modifier
                    .size(26.dp)
                    .clickable {
                        onEvent(DrawModeEvent.OnToolbarItemClicked(colorItem))
                    }
            )
        } else {
            Image(
                painter = ColorPainter(selectedColor),
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(color = MaterialTheme.colorScheme.onBackground)
                    .padding(1.dp)
                    .clip(CircleShape)
                    .size(26.dp)
                    .clickable {
                        onEvent(DrawModeEvent.OnToolbarItemClicked(colorItem))
                    }
            )
        }


        Spacer(modifier = Modifier.size(4.dp))

        Text(
            style = labelTextStyle,
            text = stringResource(id = R.string.color)
        )
    }
}