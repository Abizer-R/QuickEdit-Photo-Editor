package com.abizer_r.touchdraw.ui.transformableViews.base

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Composable
fun TransformableBox(
    modifier: Modifier = Modifier,
    viewState: TransformableBoxState,
    onEvent: (TransformableBoxEvents) -> Unit,
    content: @Composable () -> Unit
) {

    val localDensity = LocalDensity.current

    Box(
        modifier = modifier
            .offset(
                (viewState.positionOffset.x / localDensity.density).dp,
                (viewState.positionOffset.y / localDensity.density).dp,
            )
            .scale(viewState.scale)
            .rotate(viewState.rotation)
            .pointerInput(Unit) {
                detectTransformGestures { centroid, pan, zoom, rotationChange ->
                    Log.e(
                        "TEST_TransformBox", "DraggableParentView: " +
                                "\ncentroid: $centroid" +
                                "\npan: $pan" +
                                "\nzoom: $zoom" +
                                "\nrotation: $rotationChange"
                    )

                    onEvent(
                        TransformableBoxEvents.OnDrag(
                            id = viewState.id,
                            dragAmount = pan
                        )
                    )

                    onEvent(

                        TransformableBoxEvents.OnZoom(
                            id = viewState.id,
                            zoomAmount = zoom
                        )

                    )

                    onEvent(
                        TransformableBoxEvents.OnRotate(
                            id = viewState.id,
                            rotationChange = rotationChange
                        )
                    )

                }
            }

    ) {
        content()
    }
}