package com.abizer_r.quickedit.ui.transformableViews.base

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.OpenInFull
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.coerceIn
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.abizer_r.quickedit.theme.QuickEditTheme
import com.abizer_r.quickedit.ui.transformableViews.TransformableTextBox
import com.abizer_r.quickedit.utils.drawMode.DrawModeUtils
import com.abizer_r.quickedit.utils.drawMode.toPx

@Composable
fun TransformableBox(
    modifier: Modifier = Modifier,
    viewState: TransformableBoxState,
    showBorderOnly: Boolean = false,
    onEvent: (TransformableBoxEvents) -> Unit,
    content: @Composable () -> Unit
) {

    val localDensity = LocalDensity.current
    val isSelected = viewState.isSelected

    val outerBoxModifier = modifier
        .addTransformation(viewState, localDensity)
//        .addTransformation(viewState, localDensity, onEvent)
//
    ConstraintLayout(
        modifier = outerBoxModifier
    ) {
        val (btnClose, contentBox, btnScale) = createRefs()

        CloseButton(
            modifier = Modifier
                .constrainAs(btnClose) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                },
            viewState = viewState,
            showBorderOnly = showBorderOnly,
            onEvent = onEvent
        )


        var innerBoxModifier = Modifier.constrainAs(contentBox) {
            top.linkTo(btnClose.bottom)
            start.linkTo(btnClose.end)
            end.linkTo(btnScale.start)
            bottom.linkTo(btnScale.top)
        }

        if (isSelected) {
            // we need to keep the border width similar regardless the scale (zoom)
            val borderStrokeWidth = 1.dp.toPx() / viewState.scale
            val borderColor = if (showBorderOnly) Color.White else Color.Transparent
            innerBoxModifier = innerBoxModifier.dashedBorder(
                strokeWidthInPx = borderStrokeWidth,
                color = borderColor,
                cornerRadiusDp = 0.dp,
                isDashedBorder = true,
                dashOnOffSizePair = Pair(2.dp.toPx(), 2.dp.toPx())
            )
        }

        val innerBoxPaddingHorizontal = (8.dp / viewState.scale)
        val innerBoxPaddingVertical = (4.dp / viewState.scale)
        Box(
            modifier = innerBoxModifier
                .detectGestures(viewState, onEvent)
                .detectTap(viewState, onEvent)
                .padding(
                    horizontal = innerBoxPaddingHorizontal,
                    vertical = innerBoxPaddingVertical
                )
                .alpha(if (showBorderOnly) 0f else 1f)
        ) {
            content()
        }

//        ScaleButton(
//            modifier = Modifier
//                .constrainAs(btnScale) {
//                    end.linkTo(parent.end)
//                    bottom.linkTo(parent.bottom)
//                },
//            viewState = viewState,
//            showBorderOnly = showBorderOnly,
//            onEvent = onEvent
//        )
    }
}

@Composable
fun CloseButton(
    modifier: Modifier,
    viewState: TransformableBoxState,
    showBorderOnly: Boolean,
    onEvent: (TransformableBoxEvents) -> Unit
) {
    val btnAlpha = if (showBorderOnly) 1f else 0f
    val bgColor = if (showBorderOnly) MaterialTheme.colorScheme.onBackground else Color.Transparent
    val iconTintColor = MaterialTheme.colorScheme.background
    val btnOffset = (4.dp * viewState.scale).coerceIn(4.dp, 10.dp)

    Image(
        modifier = modifier
            .offset(
                x = btnOffset,
                y = btnOffset
            )
            .size(28.dp)
            .scale(1 / viewState.scale) /* to keep the size of button constant */
            .clip(CircleShape)
            .background(bgColor)
            .alpha(btnAlpha)
            .clickable {
                onEvent(
                    TransformableBoxEvents.OnCloseClicked(viewState.id)
                )
            },
        imageVector = Icons.Default.Close,
        contentScale = ContentScale.FillBounds,
        contentDescription = null,
        colorFilter = ColorFilter.tint(
            color = iconTintColor
        )
    )
}

@Composable
fun ScaleButton(
    modifier: Modifier,
    viewState: TransformableBoxState,
    showBorderOnly: Boolean,
    onEvent: (TransformableBoxEvents) -> Unit
) {
    val btnAlpha = if (showBorderOnly) 1f else 0f
    val bgColor = if (showBorderOnly) MaterialTheme.colorScheme.onBackground else Color.Transparent
    val iconTintColor = MaterialTheme.colorScheme.background
    val btnOffset = (4.dp * viewState.scale).coerceIn(4.dp, 10.dp)

    Image(
        modifier = modifier
            .offset(
                x = -btnOffset,
                y = -btnOffset
            )
            .size(28.dp)
            .scale(1 / viewState.scale) /* to keep the size of button constant */
            .clip(CircleShape)
            .background(bgColor)
            .rotate(80f)
            .alpha(btnAlpha)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    Log.e(
                        "TEST_SCALE",
                        "ScaleButton: dragDistance = ${dragAmount.getDistance() / viewState.scale}, dragAmount = $dragAmount"
                    )

                    /**
                     *
                     * think about some logic to get the appropriate zoom amount
                     *  according the drag direction and distance
                     *  => x and y both positive = zoom out
                     *  => x and y both negative = zoom in
                     *  => else = ignore
                     *
                     *
                     *
                     */

                }
            }
        ,
        imageVector = Icons.Default.OpenInFull,
        contentScale = ContentScale.Inside,
        contentDescription = null,
        colorFilter = ColorFilter.tint(
            color = iconTintColor
        )
    )
}


fun Modifier.addTransformation(
    viewState: TransformableBoxState,
    localDensity: Density
): Modifier = run {
    this.then(
        Modifier
            .offset(
                (viewState.positionOffset.x / localDensity.density).dp,
                (viewState.positionOffset.y / localDensity.density).dp,
            )
            .scale(viewState.scale)
            .rotate(viewState.rotation)

    )
}


fun Modifier.detectTap(
    viewState: TransformableBoxState,
    onEvent: (TransformableBoxEvents) -> Unit
): Modifier = run {
    this.then(
        Modifier.pointerInput(Unit) {
            detectTapGestures {
                Log.e("TEST_TransformBox", "detectTouch: TAPPED", )
                onEvent(
                    TransformableBoxEvents.OnTapped(
                        id = viewState.id, textViewState = null
                    )
                )
            }
        }
    )
}

fun Modifier.detectGestures(
    viewState: TransformableBoxState,
    onEvent: (TransformableBoxEvents) -> Unit,
): Modifier = run {
    this.then(
        Modifier.pointerInput(Unit) {
            detectTransformGestures { centroid, pan, zoom, rotationChange ->
                Log.e(
                    "TEST_TransformBox", "DraggableParentView: " +
                            "\ncentroid: $centroid" +
                            "\npan: $pan" +
                            "\nzoom: $zoom" +
                            "\nrotation: $rotationChange"
                )

                val dragAmount = pan * viewState.scale
                val rotatedDragAmount = DrawModeUtils.rotateOffset(dragAmount, viewState.rotation)
                onEvent(
                    TransformableBoxEvents.OnDrag(
                        id = viewState.id,
                        dragAmount = rotatedDragAmount  // multiply with scale to get the actual drag amount (see commit message)
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
    )
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
fun PreviewTextItem_NO_BORDER() {
    QuickEditTheme {
        Box(
            modifier = Modifier
                .size(300.dp, 100.dp)
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

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewTextItem_WITH_BORDER() {
    QuickEditTheme {
        Box(
            modifier = Modifier
                .size(300.dp, 100.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {
            val viewState = TransformableTextBoxState(
                id = "",
                text = "Hello",
                textAlign = TextAlign.Center,
                positionOffset = Offset(100f, 100f),
                scale = 1f,
                rotation = 0f,
                textColor = MaterialTheme.colorScheme.onBackground,
                textFont = MaterialTheme.typography.headlineMedium.fontSize // defaultTextFont in "TextModeScreen"
            )
            TransformableTextBox(
                viewState = viewState,
                onEvent = {},
            )
            TransformableTextBox(
                viewState = viewState,
                showBorderOnly = true,
                onEvent = {},
            )
        }
    }
}