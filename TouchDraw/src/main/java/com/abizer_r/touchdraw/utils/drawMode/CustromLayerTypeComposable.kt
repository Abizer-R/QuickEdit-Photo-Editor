package com.abizer_r.touchdraw.utils.drawMode

import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun CustomLayerTypeComposable(
    modifier: Modifier = Modifier,
    layerType: Int = View.LAYER_TYPE_HARDWARE,
    content: @Composable () -> Unit
) {
    AndroidView(
        factory = { context ->
            ComposeView(context).apply {
                setLayerType(layerType, null)
            }
        },
        update = { composeView ->
            composeView.setContent(content)
        },
        modifier = modifier
    )
}