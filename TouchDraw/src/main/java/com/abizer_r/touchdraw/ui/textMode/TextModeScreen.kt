package com.abizer_r.touchdraw.ui.textMode

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.abizer_r.components.theme.SketchDraftTheme
import com.abizer_r.components.theme.TextInputBackgroundColor
import com.abizer_r.components.theme.ToolBarBackgroundColor
import com.abizer_r.touchdraw.R
import com.abizer_r.touchdraw.ui.drawMode.DrawModeViewModel
import com.abizer_r.touchdraw.ui.drawMode.stateHandling.TextModeEvent
import com.abizer_r.touchdraw.ui.drawMode.stateHandling.TextModeEvent.*
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.BottomToolBar
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.state.BottomToolbarEvent
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.state.BottomToolbarItem
import com.abizer_r.touchdraw.ui.editorScreen.topToolbar.TextModeTopToolbar
import com.abizer_r.touchdraw.utils.textMode.TextModeUtils

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
        val (topToolBar, bottomToolbar, imageBitmapView, textInputView) = createRefs()

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
                /**
                 * TODO: State Screenshot and return (similar to DrawModeScreen)
                 */
                onDoneClicked(state.textFieldValue)
            }
        )

        Image(
            modifier = Modifier.constrainAs(imageBitmapView) {
                top.linkTo(topToolBar.bottom)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            },
            bitmap = imageBitmap,
            contentScale = ContentScale.Fit,
            contentDescription = null,
            alpha = if (state.isTextFieldVisible) 0.3f else 1f
        )

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

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewTextModeScreen() {
    SketchDraftTheme {
        TextModeScreen(
            imageBitmap = ImageBitmap.imageResource(id = R.drawable.placeholder_image_1),
            onDoneClicked = {},
            onBackPressed = {}
        )
    }
}