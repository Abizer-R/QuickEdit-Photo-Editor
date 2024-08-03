package com.abizer_r.quickedit.ui.drawMode

import android.view.View
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.TransformableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import com.abizer_r.quickedit.ui.drawMode.drawingCanvas.DrawingCanvas
import com.abizer_r.quickedit.ui.drawMode.stateHandling.DrawModeEvent
import com.abizer_r.quickedit.ui.drawMode.stateHandling.DrawModeState
import com.abizer_r.quickedit.utils.drawMode.CustomLayerTypeComposable
import com.abizer_r.quickedit.utils.other.bitmap.ImmutableBitmap

@Composable
fun DrawingCanvasContainer(
    state: DrawModeState,
    immutableBitmap: ImmutableBitmap,
    scale: Float,
    offset: Offset,
    transformableState: TransformableState,
    onOffsetChange: (Offset) -> Unit,
    onDrawingEvent: (DrawModeEvent) -> Unit
) {

    val bitmap = immutableBitmap.bitmap
    val aspectRatio = remember(bitmap) {
        bitmap.width.toFloat() / bitmap.height.toFloat()
    }

    Image(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale,
                translationX = offset.x,
                translationY = offset.y
            ),
        bitmap = bitmap.asImageBitmap(),
        contentScale = ContentScale.Fit,
        contentDescription = null
    )

    CustomLayerTypeComposable(
        layerType = View.LAYER_TYPE_HARDWARE,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            DrawingCanvas(
                modifier = Modifier
                    .aspectRatio(aspectRatio)
                    .align(Alignment.Center),
                pathDetailStack = state.pathDetailStack,
                selectedColor = state.selectedColor,
                currentTool = state.selectedTool,
                aspectRatio = aspectRatio,
                scale = scale,
                offset = offset,
                transformableState = transformableState,
                onDrawingEvent = onDrawingEvent,
                onOffsetChange = onOffsetChange
            )
        }
    }

}