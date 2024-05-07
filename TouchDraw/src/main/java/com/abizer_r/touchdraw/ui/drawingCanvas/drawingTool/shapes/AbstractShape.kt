package com.abizer_r.touchdraw.ui.drawingCanvas.drawingTool.shapes

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.abizer_r.touchdraw.ui.drawingCanvas.models.PaintValues
import com.abizer_r.touchdraw.utils.DrawingConstants

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