package com.abizer_r.touchdraw.ui.drawMode.drawingCanvas.drawingTool.shapes

import androidx.compose.ui.graphics.Color
import com.abizer_r.touchdraw.utils.drawMode.DrawingConstants

abstract class AbstractShape: BaseShape {
    var mColor: Color = Color.White
    var mWidth: Float = DrawingConstants.DEFAULT_STROKE_WIDTH
    var mAlpha: Float = DrawingConstants.DEFAULT_STROKE_ALPHA

    fun updatePaintValues(
        color: Color? = null,
        width: Float? = null,
        alpha: Float? = null
    ) {
        color?.let { mColor = it }
        width?.let { mWidth = it }
        alpha?.let { mAlpha = it }
    }
}