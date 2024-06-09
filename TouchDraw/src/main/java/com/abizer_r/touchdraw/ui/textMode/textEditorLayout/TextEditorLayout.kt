package com.abizer_r.touchdraw.ui.textMode.textEditorLayout

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.abizer_r.components.R
import com.abizer_r.components.ui.tool_items.ColorListFullWidth
import com.abizer_r.components.util.ImmutableList
import com.abizer_r.touchdraw.ui.textMode.TextModeEvent
import com.abizer_r.touchdraw.ui.textMode.TextModeState
import com.abizer_r.touchdraw.ui.textMode.getSelectedColor
import com.abizer_r.touchdraw.utils.textMode.TextModeUtils
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
    val textFontSize = MaterialTheme.typography.headlineMedium.fontSize

    val onColorItemClicked = remember<(Int, Color) -> Unit> {{ index, color ->
        onTextModeEvent(TextModeEvent.SelectTextColor(index, color))
    }}

    val onTextFieldValueChange = remember<(TextFieldValue) -> Unit> {{ mValue ->
        onTextModeEvent(TextModeEvent.UpdateTextFieldValue(mValue.text))
    }}

    ConstraintLayout(
        modifier = modifier
    ) {
        val (textField, placeHolderText, colorList) = createRefs()
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
                fontSize = textFontSize
            ),
        )

        // PlaceHolder Text
        if (textFieldState.text.isEmpty()) {
            Log.e("TEST_BLUR", "PlaceHolder Text: ", )
            Text(
                modifier = Modifier
                    .constrainAs(placeHolderText) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.wrapContent
                        height = Dimension.wrapContent
                    },
                text = stringResource(id = R.string.enter_your_text),
                textAlign = textFieldState.textAlign,
                color = textFieldState.getSelectedColor(),
                fontSize = textFontSize
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
            selectedIndex = textFieldState.selectedColorIndex,
            onItemClicked = onColorItemClicked
        )

    }
}