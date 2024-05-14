package com.abizer_r.touchdraw.ui.drawingView

import android.view.View
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.abizer_r.touchdraw.ui.drawingCanvas.DrawingCanvas
import com.abizer_r.touchdraw.ui.drawingCanvas.DrawingEvents
import com.abizer_r.touchdraw.ui.drawingCanvas.models.PathDetails
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.state.BottomToolbarItem
import com.abizer_r.touchdraw.ui.transformableViews.TransformableTextView
import com.abizer_r.touchdraw.ui.transformableViews.TransformableViewType
import com.abizer_r.touchdraw.ui.transformableViews.base.TransformableBoxEvents
import com.abizer_r.touchdraw.ui.transformableViews.base.TransformableBoxState
import com.abizer_r.touchdraw.utils.CustomLayerTypeComposable
import java.util.Stack

@Composable
fun DrawingView(
    modifier: Modifier = Modifier,
    pathDetailStack: Stack<PathDetails>,
    selectedColor: Color,
    currentTool: BottomToolbarItem,
    transformableViewsList: ArrayList<TransformableViewType>,
    onDrawingEvent: (DrawingEvents) -> Unit,
    onTransformViewEvent: (TransformableBoxEvents) -> Unit
) {

    Box(modifier = modifier) {


        CustomLayerTypeComposable(
            layerType = View.LAYER_TYPE_HARDWARE,
            modifier = Modifier.fillMaxSize()
        ) {
            DrawingCanvas(
                pathDetailStack = pathDetailStack,
                selectedColor = selectedColor,
                currentTool = currentTool,
                onDrawingEvent = onDrawingEvent
            )
        }


        transformableViewsList.forEach { mViewDetail ->
            when (mViewDetail) {
                is TransformableViewType.TextTransformable -> {
                    TransformableTextView(
                        viewDetail = mViewDetail,
                        onEvent = onTransformViewEvent
                    )
                }
            }
        }
    }
}