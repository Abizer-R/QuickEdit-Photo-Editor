package com.abizer_r.touchdraw.ui.transformableViews.base

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.abizer_r.components.theme.SketchDraftTheme
import com.abizer_r.touchdraw.ui.transformableViews.TransformableTextView
import com.abizer_r.touchdraw.ui.transformableViews.TransformableViewType
import com.abizer_r.touchdraw.utils.pxToDp
import com.abizer_r.touchdraw.utils.toPx

@Composable
fun TransformableBox(
    modifier: Modifier = Modifier,
    viewState: TransformableBoxState,
    onEvent: (TransformableBoxEvents) -> Unit,
    content: @Composable () -> Unit
) {

    val localDensity = LocalDensity.current
    // we need to keep the border width similar regardless the scale (zoom)
    val borderStrokeWidth = 1.dp.toPx() / viewState.scale

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
            .dashedBorder(
                strokeWidthInPx = borderStrokeWidth,
                color = MaterialTheme.colorScheme.onBackground,
                cornerRadiusDp = 0.dp,
                isDashedBorder = true,
                dashOnOffSizePair = Pair(2.dp.toPx(), 2.dp.toPx())
            )
            .padding(8.dp)

    ) {
        content()
    }
}

@Composable
fun Modifier.dashedBorder(
    strokeWidthInPx: Float,
    color: Color,
    cornerRadiusDp: Dp,
    isDashedBorder: Boolean = false,
    dashOnOffSizePair: Pair<Float, Float> = Pair(10f, 10f)
) = composed(
    factory = {
        val density = LocalDensity.current
        val cornerRadiusPx = density.run { cornerRadiusDp.toPx() }

        this.then(
            Modifier.drawWithCache {
                onDrawBehind {
                    val stroke = Stroke(
                        width = strokeWidthInPx,
                        pathEffect = if (isDashedBorder) {
                            PathEffect.dashPathEffect(
                                intervals = floatArrayOf(dashOnOffSizePair.first, dashOnOffSizePair.second),
                                phase = 0f
                            )
                        } else null
                    )

                    drawRoundRect(
                        color = color,
                        style = stroke,
                        cornerRadius = CornerRadius(cornerRadiusPx)
                    )
                }
            }
        )
    }
)

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewTextItem() {
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