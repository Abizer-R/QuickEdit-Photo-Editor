package com.abizer_r.touchdraw.ui.draggableView

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abizer_r.components.theme.SketchDraftTheme

@Composable
fun DraggableParentView(modifier: Modifier) {

    val localDensity = LocalDensity.current

    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    Box(modifier = modifier) {


        /**
         * The draggable Box
         */
        Box(
            modifier = Modifier
                .offset(
                    (offsetX / localDensity.density).dp,
                    (offsetY / localDensity.density).dp,
                )
                .size(100.dp)
                .clip(shape = RoundedCornerShape(10.dp))
                .background(Color.Gray)
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        offsetX += dragAmount.x
                        offsetY += dragAmount.y
                    }
                }

        ) {

        }
    }
}

@Composable
@Preview
fun Preview() {
    SketchDraftTheme {
        DraggableParentView(modifier = Modifier.fillMaxSize())
    }
}