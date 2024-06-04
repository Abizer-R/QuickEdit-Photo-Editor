package com.abizer_r.touchdraw.ui.transformableViews

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.abizer_r.components.theme.SketchDraftTheme
import com.abizer_r.touchdraw.ui.transformableViews.base.TransformableBox
import com.abizer_r.touchdraw.ui.transformableViews.base.TransformableBoxEvents
import com.abizer_r.touchdraw.ui.transformableViews.base.TransformableBoxState

@Composable
fun TransformableTextView(
    modifier: Modifier = Modifier,
    showBorderOnly: Boolean = false,
    viewDetail: TransformableViewType.TextTransformable,
    onEvent: (TransformableBoxEvents) -> Unit
) {

    TransformableBox(
        modifier = modifier,
        viewState = viewDetail.viewState,
        showBorderOnly = showBorderOnly,
        onEvent = onEvent
    ) {
        Text(
            text = viewDetail.text,
            style = TextStyle(
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = MaterialTheme.typography.titleLarge.fontSize
            )
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewItem() {
    SketchDraftTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            TransformableTextView(
                viewDetail = TransformableViewType.TextTransformable(
                    text = "Hello",
                    viewState = TransformableBoxState(
                        id = "",
                        positionOffset = Offset(100f, 100f),
                        scale = 1f,
                        rotation = 0f
                    )
                ),
                onEvent = {},
            )
        }
    }
}