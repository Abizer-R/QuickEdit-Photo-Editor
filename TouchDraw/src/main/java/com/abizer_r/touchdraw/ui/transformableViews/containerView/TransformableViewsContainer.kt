package com.abizer_r.touchdraw.ui.transformableViews.containerView

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abizer_r.components.theme.SketchDraftTheme
import com.abizer_r.touchdraw.ui.transformableViews.childView.TransformableBox
import com.abizer_r.touchdraw.ui.transformableViews.childView.TransformableBoxEvents
import com.abizer_r.touchdraw.ui.transformableViews.childView.TransformableBoxState

@Composable
fun TransformableViewsContainer(
    modifier: Modifier,
    transformableBoxList: ArrayList<TransformableBoxState>,
    onTransformableBoxEvents: (TransformableBoxEvents) -> Unit
) {

//    val localDensity = LocalDensity.current

    val boxSize = 100.dp

    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var rotation by remember { mutableStateOf(1f) }

//    var offsetX by remember { mutableStateOf(0f) }
//    var offsetY by remember { mutableStateOf(0f) }


    Log.e("TEST_Event", "DraggableParentView: drawing all boxes", )
    Box(modifier = modifier) {


        transformableBoxList.forEach { mViewState ->
            TransformableBox(
                viewState = mViewState,
                onTransformableBoxEvents
            )
        }
        /**
         * The draggable Box
         */

//        Box(
//            modifier = Modifier
//                .offset(
//                    (offset.x / localDensity.density).dp,
//                    (offset.y / localDensity.density).dp,
//                )
//                .size(boxSize * scale)
//                .rotate(rotation)
//                .clip(shape = RoundedCornerShape(10.dp))
//                .background(Color.Gray)
////                .pointerInput(Unit) {
////                    detectDragGestures { change, dragAmount ->
////                        change.consume()
////                        offsetX += dragAmount.x
////                        offsetY += dragAmount.y
////                    }
////                }
//                .pointerInput(Unit) {
//                    detectTransformGestures { centroid, pan, zoom, rotationChange ->
//                        Log.e(
//                            "TEST", "DraggableParentView: " +
//                                    "\ncentroid: $centroid" +
//                                    "\npan: $pan" +
//                                    "\nzoom: $zoom" +
//                                    "\nrotation: $rotation"
//                        )
//                        scale = (scale * zoom).coerceIn(1f, 5f)
//                        offset += pan
//                        rotation += rotationChange
//                    }
//                }
//
//        ) {
//
//        }
    }
}

@Composable
@Preview
fun Preview() {
    SketchDraftTheme {
        TransformableViewsContainer(
            modifier = Modifier.fillMaxSize(),
            transformableBoxList = arrayListOf(
                TransformableBoxState("", Offset(0f,0f), 100.dp, 1f, 1f)
            ),
            onTransformableBoxEvents = {}
        )
    }
}