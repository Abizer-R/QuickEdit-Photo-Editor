package com.abizer_r.touchdraw.ui.textMode

import android.graphics.Bitmap
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.abizer_r.components.util.defaultErrorToast
import com.abizer_r.touchdraw.ui.textMode.stateHandling.TextModeEvent.*
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.BottomToolBar
import com.abizer_r.touchdraw.ui.editorScreen.topToolbar.TextModeTopToolbar
import com.abizer_r.touchdraw.ui.transformableViews.TransformableTextBox
import com.abizer_r.touchdraw.ui.transformableViews.base.TransformableTextBoxState
import com.abizer_r.touchdraw.ui.transformableViews.base.TransformableBoxEvents
import com.abizer_r.touchdraw.ui.transformableViews.base.TransformableBoxState
import com.abizer_r.touchdraw.utils.textMode.TextModeUtils
import com.skydoves.cloudy.Cloudy
import com.smarttoolfactory.screenshot.ImageResult
import com.smarttoolfactory.screenshot.ScreenshotBox
import com.smarttoolfactory.screenshot.rememberScreenshotState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TextModeScreen(
    modifier: Modifier = Modifier,
    bitmap: ImageBitmap,
    onDoneClicked: (bitmap: Bitmap) -> Unit,
    onBackPressed: () -> Unit
) {
    val context = LocalContext.current
    val lifeCycleOwner = LocalLifecycleOwner.current

    val viewModel: TextModeViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle(
        lifecycleOwner = lifeCycleOwner
    )
    val bottomToolbarState by viewModel.bottomToolbarState.collectAsStateWithLifecycle(
        lifecycleOwner = lifeCycleOwner
    )

    val focusRequesterForTextField = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    DisposableEffect(key1 = Unit) {
        onDispose {
            keyboardController?.hide()
        }
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
        keyboardController?.hide()
        onBackPressed()
    }


    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .imePadding()
    ) {
        val (topToolBar, bottomToolbar, editorBox, editorBoxBgStretched, textInputView) = createRefs()

        TextModeTopToolbar(
            modifier = Modifier.constrainAs(topToolBar) {
                top.linkTo(parent.top)
                width = Dimension.matchParent
                height = Dimension.wrapContent
            },
            onCloseClicked = {
                if (state.textFieldState.isVisible) {
                    viewModel.onEvent(HideTextField)
                } else {
                    onBackPressed()
                }
            },
            onDoneClicked = {
                if (state.textFieldState.isVisible) {
                    viewModel.onEvent(AddTransformableTextBox(
                        textBoxState = TransformableTextBoxState(
                            id = state.textFieldState.textStateId ?: UUID.randomUUID().toString(),
                            text = state.textFieldState.text,
                            textColor = state.textFieldState.textColor,
                            textAlign = state.textFieldState.textAlign
                        )
                    ))
                    viewModel.onEvent(HideTextField)
                } else {
                    viewModel.shouldGoToNextScreen = true
                    lifeCycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                        viewModel.updateViewSelection(null)
                        delay(200)  /* Delay to update the selection in ui */
                        screenshotState.capture()
                    }
                }
            }
        )
        val aspectRatio = bitmap.let {
            bitmap.width.toFloat() / bitmap.height.toFloat()
        }
        ScreenshotBox(
            modifier = Modifier
                .constrainAs(editorBox) {
                    top.linkTo(topToolBar.bottom)
                    bottom.linkTo(bottomToolbar.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.ratio(aspectRatio.toString())
                    height = Dimension.fillToConstraints
                }
                .clipToBounds(),
            screenshotState = screenshotState
        ) {

            BitmapBackground(
                modifier = Modifier.fillMaxSize(),
                imageBitmap = bitmap,
                isTextFieldVisible = state.textFieldState.isVisible,
                onBgClicked = {
                    viewModel.updateViewSelection(null)
                }
            )

            Box(modifier = Modifier.fillMaxSize()) {
                if (state.textFieldState.isVisible.not()) {
                    DrawAllTransformableViews(
                        centerAlignModifier = Modifier.align(Alignment.Center),
                        transformableViewsList = state.transformableViewStateList,
                        onTransformableBoxEvent = {
                            viewModel.onTransformableBoxEvent(it)
                        }
                    )
                }
            }

        }

        Box(
            modifier = Modifier
                .constrainAs(editorBoxBgStretched) {
                    top.linkTo(topToolBar.bottom)
                    bottom.linkTo(bottomToolbar.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
                .clipToBounds()
        ) {


            if (state.textFieldState.isVisible.not()) {
                BorderForSelectedViews(
                    centerAlignModifier = Modifier.align(Alignment.Center),
                    transformableViewsList = state.transformableViewStateList,
                    onTransformableBoxEvent = {
                        viewModel.onTransformableBoxEvent(it)
                    }
                )
            }

        }

        if (state.textFieldState.isVisible) {
            TextField(
                modifier = Modifier
                    .constrainAs(textInputView) {
                        top.linkTo(topToolBar.bottom)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.wrapContent
                        height = Dimension.wrapContent
                    }
                    .background(Color.Transparent)
                    .focusRequester(focusRequesterForTextField),
                value = TextFieldValue(
                    text = state.textFieldState.text,
                    selection = TextRange(state.textFieldState.text.length)
                ),
                onValueChange = { mValue ->
                    viewModel.onEvent(UpdateTextFieldValue(mValue.text))
                },
                colors = TextModeUtils.getColorsForTextField(),
                textStyle = TextStyle(
                    textAlign = TextAlign.Center,
                    fontSize = MaterialTheme.typography.headlineMedium.fontSize
                )
            )
            if (viewModel.shouldRequestFocus) {
                LaunchedEffect(key1 = Unit) {
                    focusRequesterForTextField.requestFocus()
                }
            }
        }


        BottomToolBar(
            modifier = Modifier.constrainAs(bottomToolbar) {
                bottom.linkTo(parent.bottom)
                width = Dimension.matchParent
                height = Dimension.wrapContent
            },
            bottomToolbarState = bottomToolbarState,
            onEvent = {
                viewModel.onBottomToolbarEvent(it)
//                if (
//                    it is BottomToolbarEvent.OnItemClicked &&
//                    it.toolbarItem == BottomToolbarItem.AddItem
//                ) {
//                    viewModel.shouldRequestFocus = true
//                }
            }
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BitmapBackground(
    modifier: Modifier,
    imageBitmap: ImageBitmap,
    isTextFieldVisible: Boolean,
    onBgClicked: () -> Unit
) {
    /**
     * WORK AROUND - depending on case (textField visible or not), adding/removing a "Cloudy" composable
     * REASON - Seems like the "Cloudy" composable is converting the view to bitmap before blurring (or something like that)
     *          So, when the textField is not visible, there is a permanent image of the newly created textBox with the selection layout.
     *          This creates a bug, and I'm not able to figure out how to prevent this
     */
    if (isTextFieldVisible) {
        Cloudy(
            modifier = modifier,
            radius = 15
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                bitmap = imageBitmap,
                contentScale = ContentScale.Inside,
                contentDescription = null,
                alpha = 0.3f
            )
        }

    } else {
        Image(
            modifier = Modifier
                .fillMaxSize()
                .pointerInteropFilter {
                    onBgClicked()
//                        viewModel.updateViewSelection(null)
                    true
                },
            bitmap = imageBitmap,
            contentScale = ContentScale.Fit,
            contentDescription = null,
            alpha = 1f
        )
    }
}

@Composable
fun DrawAllTransformableViews(
    centerAlignModifier: Modifier,
    transformableViewsList: ArrayList<TransformableBoxState>,
    onTransformableBoxEvent: (event: TransformableBoxEvents) -> Unit
) {
    transformableViewsList.forEach { mViewState ->
        when (mViewState) {
            is TransformableTextBoxState -> {
                TransformableTextBox(
                    modifier = centerAlignModifier,
                    viewState = mViewState,
                    onEvent = onTransformableBoxEvent
                )
            }
        }
    }
}


@Composable
fun BorderForSelectedViews(
    centerAlignModifier: Modifier,
    transformableViewsList: ArrayList<TransformableBoxState>,
    onTransformableBoxEvent: (event: TransformableBoxEvents) -> Unit
) {
    transformableViewsList
        .filter { it.isSelected }
        .forEach { mViewState ->
        when (mViewState) {
            is TransformableTextBoxState -> {
                TransformableTextBox(
                    modifier = centerAlignModifier,
                    viewState = mViewState,
                    showBorderOnly = true,
                    onEvent = onTransformableBoxEvent
                )
            }
        }
    }
}