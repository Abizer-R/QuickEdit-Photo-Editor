package com.abizer_r.quickedit.ui.transformableViews

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.abizer_r.components.theme.QuickEditTheme
import com.abizer_r.quickedit.ui.transformableViews.base.TransformableTextBoxState
import com.abizer_r.quickedit.ui.transformableViews.base.TransformableBox
import com.abizer_r.quickedit.ui.transformableViews.base.TransformableBoxEvents

@Composable
fun TransformableTextBox(
    modifier: Modifier = Modifier,
    showBorderOnly: Boolean = false,
    viewState: TransformableTextBoxState,
    onEvent: (TransformableBoxEvents) -> Unit
) {

    val onEventLambda = remember<(TransformableBoxEvents) -> Unit> {{
        // Intercepting events to modify required ones
        when (it) {
            is TransformableBoxEvents.OnTapped -> onEvent(
                TransformableBoxEvents.OnTapped(it.id, viewState)
            )

            else -> onEvent(it)
        }
    }}
    TransformableBox(
        modifier = modifier,
        viewState = viewState,
        showBorderOnly = showBorderOnly,
        onEvent = onEventLambda
    ) {
        Text(
            text = viewState.text,
            style = TextStyle(
                color = viewState.textColor,
                fontSize = viewState.textFont,
                textAlign = viewState.textAlign
            )
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewItem() {
    QuickEditTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            TransformableTextBox(
                viewState = TransformableTextBoxState(
                    id = "",
                    text = "Hello",
                    textAlign = TextAlign.Center,
                    positionOffset = Offset(100f, 100f),
                    scale = 1f,
                    rotation = 0f,
                    textColor = MaterialTheme.colorScheme.onBackground,
                    textFont = MaterialTheme.typography.headlineMedium.fontSize // defaultTextFont in "TextModeScreen"
                ),
                onEvent = {},
            )
        }
    }
}