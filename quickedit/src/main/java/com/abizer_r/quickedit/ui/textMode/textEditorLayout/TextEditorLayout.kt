package com.abizer_r.quickedit.ui.textMode.textEditorLayout

import android.content.res.Configuration
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.abizer_r.quickedit.R
import com.abizer_r.quickedit.theme.QuickEditTheme
import com.abizer_r.quickedit.ui.editorScreen.bottomToolbar.TOOLBAR_HEIGHT_SMALL
import com.abizer_r.quickedit.ui.editorScreen.topToolbar.TextModeTopToolbar
import com.abizer_r.quickedit.utils.textMode.colorList.ColorListFullWidth
import com.abizer_r.quickedit.utils.ImmutableList
import com.abizer_r.quickedit.utils.textMode.TextModeUtils
import com.abizer_r.quickedit.ui.textMode.bottomToolbarExtension.textFormatOptions.alignmentOptions.TextAlignOptions
import com.abizer_r.quickedit.utils.textMode.TextModeUtils.getDefaultEditorTextStyle
import kotlinx.coroutines.delay
import java.util.UUID

@Composable
fun TextEditorLayout(
    modifier: Modifier,
    initialEditorState: TextEditorState? = null,
    onDoneClicked: (TextEditorState) -> Unit,
    onClosedClicked: () -> Unit,
) {


    /**
     *
     *
     *
     *
     *
     * TODO: make the TextEditorLayout a separate screen which will do following
     * -> input: initial TextEditorState
     * -> output: final TextEditorState
     *
     *
     *
     *
     *
     */

    val context = LocalContext.current
    val lifeCycleOwner = LocalLifecycleOwner.current


    val viewModel: TextEditorViewModel = hiltViewModel()
    val editorState by viewModel.editorState.collectAsStateWithLifecycle(
        lifecycleOwner = lifeCycleOwner
    )

    val defaultTextFont = getDefaultEditorTextStyle().fontSize
    LaunchedEffect(key1 = Unit) {
        // updating the initialState is necessary even if "initialEditorState" argument is null
        // This is because the "hiltViewModel" is scoped to navigation destination and will keep holding the previous editorState
        val initialState = initialEditorState ?: TextEditorState(
            textStateId = UUID.randomUUID().toString(),
            textFont = defaultTextFont
        )
        Log.d("TEST_editor", "TextEditorLayout: id = ${initialState.textStateId}", )
        viewModel.updateInitialState(initialState = initialState)
    }

    val customTextStyle = getDefaultEditorTextStyle().copy(
        color = editorState.selectedColor,
        textAlign = editorState.textAlign,
        fontSize = editorState.textFont
    )
    val focusRequesterForTextField = remember { FocusRequester() }

    val topToolbarHeight =  TOOLBAR_HEIGHT_SMALL

    val onDoneClickedLambda = remember<() -> Unit> {{
        onDoneClicked(editorState)
    }}

    val showPlaceHolder by remember(key1 = editorState.text.isBlank()) {
        Log.e("TEST_text_mode", "TextEditorLayout: showPlaceHolder = ${editorState.text.isBlank()}", )
        mutableStateOf(editorState.text.isBlank())
    }

    val onColorItemClicked = remember<(Int, Color) -> Unit> {{ index, color ->
        viewModel.onEvent(TextEditorEvent.SelectTextColor(index, color))
    }}

    val onTextAlignItemClicked = remember<(Int, TextAlign) -> Unit> {{ index, textAlign ->
        viewModel.onEvent(TextEditorEvent.SelectTextAlign(index, textAlign))
    }}

    val onTextFieldValueChange = remember<(TextFieldValue) -> Unit> {{ mValue ->
        viewModel.onEvent(TextEditorEvent.UpdateTextFieldValue(mValue.text))
    }}

    ConstraintLayout(
        modifier = modifier
    ) {
        val (topToolBar, textField, placeHolderText, colorList, textAlignOptions) = createRefs()

        TextModeTopToolbar(
            modifier = Modifier.constrainAs(topToolBar) {
                top.linkTo(parent.top)
                width = Dimension.matchParent
                height = Dimension.wrapContent
            },
            toolbarHeight = topToolbarHeight,
            onCloseClicked = onClosedClicked,
            onDoneClicked = onDoneClickedLambda
        )

        TextField(
            modifier = Modifier
                .constrainAs(textField) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.wrapContent
                    height = Dimension.wrapContent
                }
                .padding(top = topToolbarHeight)
                .background(Color.Transparent)
                .focusRequester(focusRequesterForTextField),
            value = TextFieldValue(
                text = editorState.text,
                selection = TextRange(editorState.text.length)
            ),
            onValueChange = onTextFieldValueChange,
            colors = TextModeUtils.getColorsForTextField(
                cursorColor = editorState.selectedColor
            ),
            textStyle = customTextStyle,
        )

        // PlaceHolder Text
        AnimatedVisibility(
            visible = showPlaceHolder,
            modifier = Modifier
                .constrainAs(placeHolderText) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.matchParent
                    height = Dimension.wrapContent
                }
                .padding(top = topToolbarHeight),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Text(
                text = stringResource(id = R.string.enter_your_text),
                style = customTextStyle,
            )
        }

        if (editorState.shouldRequestFocus) {
            LaunchedEffect(key1 = Unit) {
                delay(100)  /* Delay to allow textField to become visible */
                focusRequesterForTextField.requestFocus()
            }
        }


        ColorListFullWidth(
            modifier = Modifier.constrainAs(colorList) {
                bottom.linkTo(parent.bottom)
                width = Dimension.matchParent
                height = Dimension.wrapContent
            },
            colorList = ImmutableList(editorState.textColorList),
            backgroundColor = Color.Transparent,
            selectedColor = editorState.selectedColor,
            onItemClicked = onColorItemClicked
        )

        TextAlignOptions(
            modifier = Modifier.constrainAs(textAlignOptions) {
                bottom.linkTo(colorList.top)
                start.linkTo(parent.start)
            },
            selectedAlignment = editorState.textAlign,
            optionList = TextModeUtils.getTextAlignOptions(),
            backgroundColor = Color.Transparent,
            onItemClicked = onTextAlignItemClicked
        )

    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun Preview_TextEditorLayout() {
    QuickEditTheme {
        TextEditorLayout(
            modifier = Modifier.fillMaxSize(),
            initialEditorState = TextEditorState(
                textStateId = UUID.randomUUID().toString(),
                textFont = getDefaultEditorTextStyle().fontSize // defaultTextFont in "TextModeScreen"
            ),
            onDoneClicked = {},
            onClosedClicked = {}
        )

    }
}