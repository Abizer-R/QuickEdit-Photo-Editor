package com.abizer_r.touchdraw.ui.textInputScreen

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.abizer_r.components.theme.SketchDraftTheme
import com.abizer_r.components.theme.TextInputBackgroundColor
import com.abizer_r.components.theme.ToolBarBackgroundColor

@Composable
fun TextInputScreen(
    modifier: Modifier = Modifier,
    onDoneClicked: (String) -> Unit,
    onBackPressed: () -> Unit
) {

    var textInput by remember { mutableStateOf("") }

    val focusRequesterForTextField = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    DisposableEffect(key1 = Unit) {
        onDispose {
            keyboardController?.hide()
        }
    }

    LaunchedEffect(key1 = Unit) {
        focusRequesterForTextField.requestFocus()
    }

    BackHandler {
        onBackPressed()
    }


    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .background(TextInputBackgroundColor)
            .imePadding()
    ) {
        val (btnDone, textInputView) = createRefs()

        Card(
            modifier = Modifier
                .constrainAs(btnDone) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    width = Dimension.wrapContent
                    height = Dimension.wrapContent
                }
                .padding(16.dp)
                .clickable {
                    onDoneClicked(textInput)
                },
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground)
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                text = stringResource(id = com.abizer_r.components.R.string.done),
                textAlign = TextAlign.Center,
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = MaterialTheme.typography.labelLarge.fontSize
                )
            )
        }

        TextField(
            modifier = Modifier
                .constrainAs(textInputView) {
                    top.linkTo(btnDone.bottom)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.wrapContent
                    height = Dimension.wrapContent
                }
                .background(Color.Transparent)
                .focusRequester(focusRequesterForTextField),
            value = textInput,
            onValueChange = { mText ->
                textInput = mText
            },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent

            ),
            textStyle = TextStyle(
                textAlign = TextAlign.Center,
                fontSize = MaterialTheme.typography.headlineMedium.fontSize
            )
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewTextInputScreen() {
    SketchDraftTheme {
        TextInputScreen(
            onDoneClicked = {},
            onBackPressed = {}
        )
    }
}