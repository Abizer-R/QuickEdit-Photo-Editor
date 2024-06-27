package com.abizer_r.touchdraw.ui.textMode.textEditorLayout

import android.content.res.Configuration
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.abizer_r.components.R
import com.abizer_r.components.theme.SketchDraftTheme
import com.abizer_r.components.util.ColorUtils
import com.abizer_r.touchdraw.utils.textMode.colorList.ColorListFullWidth
import com.abizer_r.components.util.ImmutableList
import com.abizer_r.touchdraw.ui.textMode.TextModeEvent
import com.abizer_r.touchdraw.ui.textMode.TextModeState
import com.abizer_r.touchdraw.ui.textMode.getSelectedColor
import com.abizer_r.touchdraw.utils.textMode.TextModeUtils
import com.abizer_r.touchdraw.utils.textMode.alignmentOptions.TextAlignOptions
import kotlinx.coroutines.delay

@Composable
fun TextEditorLayout(
    modifier: Modifier,
    textFieldState: TextModeState.TextFieldState,
    onTextModeEvent: (TextModeEvent) -> Unit
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


    val focusRequesterForTextField = remember { FocusRequester() }

    val showPlaceHolder by remember(key1 = textFieldState.text.isBlank()) {
        Log.e("TEST_text_mode", "TextEditorLayout: showPlaceHolder = ${textFieldState.text.isBlank()}", )
        mutableStateOf(textFieldState.text.isBlank())
    }

    val onColorItemClicked = remember<(Int, Color) -> Unit> {{ index, color ->
        onTextModeEvent(TextModeEvent.SelectTextColor(index, color))
    }}

    val onTextAlignItemClicked = remember<(Int, TextAlign) -> Unit> {{ index, textAlign ->
        onTextModeEvent(TextModeEvent.SelectTextAlign(index, textAlign))
    }}

    val onTextFieldValueChange = remember<(TextFieldValue) -> Unit> {{ mValue ->
        onTextModeEvent(TextModeEvent.UpdateTextFieldValue(mValue.text))
    }}

    ConstraintLayout(
        modifier = modifier
    ) {
        val (textField, placeHolderText, colorList, textAlignOptions) = createRefs()
        TextField(
            modifier = Modifier
                .constrainAs(textField) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.matchParent
                    height = Dimension.wrapContent
                }
                .background(Color.Transparent)
//                .background(Color.Red)
                .focusRequester(focusRequesterForTextField),
            value = TextFieldValue(
                text = textFieldState.text,
                selection = TextRange(textFieldState.text.length)
            ),
            onValueChange = onTextFieldValueChange,
            colors = TextModeUtils.getColorsForTextField(
                cursorColor = textFieldState.getSelectedColor()
            ),
            textStyle = TextStyle(
                color = textFieldState.getSelectedColor(),
                textAlign = textFieldState.textAlign,
                fontSize = textFieldState.textFont
            ),
        )

        // PlaceHolder Text
        AnimatedVisibility(
            visible = showPlaceHolder,
            modifier = Modifier.constrainAs(placeHolderText) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.matchParent
                height = Dimension.wrapContent
            },
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Text(
                text = stringResource(id = R.string.enter_your_text),
                textAlign = textFieldState.textAlign,
                color = textFieldState.getSelectedColor(),
                fontSize = textFieldState.textFont
            )
        }

        if (textFieldState.shouldRequestFocus) {
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
            colorList = ImmutableList(textFieldState.textColorList),
            backgroundColor = Color.Transparent,
            selectedIndex = textFieldState.selectedColorIndex,
            onItemClicked = onColorItemClicked
        )

        TextAlignOptions(
            modifier = Modifier.constrainAs(textAlignOptions) {
                bottom.linkTo(colorList.top)
                start.linkTo(parent.start)
            },
            selectedAlignment = textFieldState.textAlign,
            optionList = TextModeUtils.getTextAlignOptions(),
            backgroundColor = Color.Transparent,
            onItemClicked = onTextAlignItemClicked
        )

    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun Preview_TextEditorLayout() {
    SketchDraftTheme {
        TextEditorLayout(
            modifier = Modifier.fillMaxSize(),
            textFieldState = TextModeState.TextFieldState(
                textFont = MaterialTheme.typography.headlineMedium.fontSize // defaultTextFont in "TextModeScreen"
            ),
            onTextModeEvent = {}
        )

    }
}