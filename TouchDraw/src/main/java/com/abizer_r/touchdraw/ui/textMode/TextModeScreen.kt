package com.abizer_r.touchdraw.ui.textMode

import android.graphics.Bitmap
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.abizer_r.components.ui.blurBackground.BlurBitmapBackground
import com.abizer_r.components.util.defaultErrorToast
import com.abizer_r.touchdraw.ui.textMode.stateHandling.TextModeEvent.*
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.BottomToolBar
import com.abizer_r.touchdraw.ui.editorScreen.topToolbar.TextModeTopToolbar
import com.abizer_r.touchdraw.ui.textMode.stateHandling.getSelectedColor
import com.abizer_r.touchdraw.ui.textMode.textEditor.TextEditorLayout
import com.abizer_r.touchdraw.ui.transformableViews.base.TransformableTextBoxState
import com.abizer_r.touchdraw.utils.textMode.TextModeUtils.BorderForSelectedViews
import com.abizer_r.touchdraw.utils.textMode.TextModeUtils.DrawAllTransformableViews
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
            viewModel.onEvent(UpdateShouldGoToNextScreen(false))
            context.defaultErrorToast()
        }
        is ImageResult.Success -> {
            if (state.shouldGoToNextScreen) {
                viewModel.onEvent(UpdateShouldGoToNextScreen(false))
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
                            textColor = state.textFieldState.getSelectedColor(),
                            textAlign = state.textFieldState.textAlign
                        )
                    ))
                    viewModel.onEvent(HideTextField)
                } else {
                    viewModel.onEvent(UpdateShouldGoToNextScreen(true))
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

            BlurBitmapBackground(
                modifier = Modifier.fillMaxSize(),
                imageBitmap = bitmap,
                shouldBlur = state.textFieldState.isVisible,
                blurRadius = 15,
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
            TextEditorLayout(
                modifier = Modifier
                    .constrainAs(textInputView) {
                        top.linkTo(topToolBar.bottom)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    },
                textFieldState = state.textFieldState,
                onTextModeEvent = {
                    viewModel.onEvent(it)
                }
            )

        }


        if (state.textFieldState.isVisible.not()) {
            BottomToolBar(
                modifier = Modifier.constrainAs(bottomToolbar) {
                    bottom.linkTo(parent.bottom)
                    width = Dimension.matchParent
                    height = Dimension.wrapContent
                },
                bottomToolbarState = bottomToolbarState,
                onEvent = {
                    viewModel.onBottomToolbarEvent(it)
                }
            )

        }
    }
}