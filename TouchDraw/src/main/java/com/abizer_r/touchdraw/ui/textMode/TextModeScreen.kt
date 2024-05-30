package com.abizer_r.touchdraw.ui.textMode

import android.util.Log
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.abizer_r.touchdraw.ui.drawMode.stateHandling.TextModeEvent.*
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.BottomToolBar
import com.abizer_r.touchdraw.ui.editorScreen.topToolbar.TextModeTopToolbar
import com.abizer_r.touchdraw.ui.transformableViews.TransformableTextView
import com.abizer_r.touchdraw.ui.transformableViews.TransformableViewType
import com.abizer_r.touchdraw.ui.transformableViews.base.TransformableBoxEvents
import com.abizer_r.touchdraw.ui.transformableViews.base.TransformableBoxState
import com.abizer_r.touchdraw.ui.transformableViews.getId
import com.abizer_r.touchdraw.ui.transformableViews.getPositionOffset
import com.abizer_r.touchdraw.ui.transformableViews.getRotation
import com.abizer_r.touchdraw.ui.transformableViews.getScale
import com.abizer_r.touchdraw.ui.transformableViews.setPositionOffset
import com.abizer_r.touchdraw.ui.transformableViews.setRotation
import com.abizer_r.touchdraw.ui.transformableViews.setScale
import com.abizer_r.touchdraw.utils.textMode.TextModeUtils
import java.util.UUID

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TextModeScreen(
    modifier: Modifier = Modifier,
    imageBitmap: ImageBitmap,
    onDoneClicked: (String) -> Unit,
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

//    LaunchedEffect(key1 = Unit) {
//        focusRequesterForTextField.requestFocus()
//    }

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
        val (topToolBar, bottomToolbar, editorBox, textInputView) = createRefs()

        TextModeTopToolbar(
            modifier = Modifier.constrainAs(topToolBar) {
                top.linkTo(parent.top)
                width = Dimension.matchParent
                height = Dimension.wrapContent
            },
            onCloseClicked = {
                if (state.isTextFieldVisible) {
                    viewModel.onEvent(HideTextField)
                } else {
                    onBackPressed()
                }
            },
            onDoneClicked = {
                if (state.isTextFieldVisible) {
                    viewModel.onEvent(AddTransformableTextView(
                        view = TransformableViewType.TextTransformable(
                            text = state.textFieldValue,
                            viewState = TransformableBoxState(id = UUID.randomUUID().toString())
                        )
                    ))
                    viewModel.onEvent(HideTextField)
                } else {
                    /**
                     * TODO: State Screenshot and return (similar to DrawModeScreen)
                     */
                    onDoneClicked(state.textFieldValue)
                }
            }
        )

        Box(
            modifier = Modifier.constrainAs(editorBox) {
                top.linkTo(topToolBar.bottom)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            }
        ) {

            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInteropFilter {
                        viewModel.updateViewSelection(null)
                        true
                    },
                bitmap = imageBitmap,
                contentScale = ContentScale.Fit,
                contentDescription = null,
                alpha = if (state.isTextFieldVisible) 0.3f else 1f
            )



            if (state.isTextFieldVisible.not()) {
                DrawAllTransformableViews(
                    transformableViewsList = state.transformableViewsList,
                    onTransformableBoxEvent = {
                        viewModel.onTransformableBoxEvent(it)
                    }
                )
            }

        }

        if (state.isTextFieldVisible) {
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
                value = state.textFieldValue,
                onValueChange = { mText ->
                    viewModel.onEvent(UpdateTextFieldValue(mText))
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

@Composable
fun DrawAllTransformableViews(
    transformableViewsList: ArrayList<TransformableViewType>,
    onTransformableBoxEvent: (event: TransformableBoxEvents) -> Unit
) {
    transformableViewsList.forEach { mViewDetail ->
        when (mViewDetail) {
            is TransformableViewType.TextTransformable -> {
                TransformableTextView(
                    viewDetail = mViewDetail,
                    onEvent = onTransformableBoxEvent
                )
            }
        }
    }
}