package com.abizer_r.touchdraw.ui.drawingCanvas.drawingTool.shapes

import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.roundToInt

class BrushShape(
    private val isEraser: Boolean = false,
    color: Color? = null,
    width: Float? = null,
    alpha: Float? = null
): AbstractShape() {

    init {
        updatePaintValues(color, width, alpha)
    }

    private var path = Path()
    private var prevOffSet = Offset.Zero

    override fun draw(drawScope: DrawScope) {
        drawScope.drawPath(
            path = path,
            brush = SolidColor(mColor),
            style = Stroke(
                width = mWidth,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            ),
            alpha = mAlpha,
            blendMode = if (isEraser) BlendMode.Clear else BlendMode.SrcOver
        )
    }

    override fun initShape(startX: Float, startY: Float) {
        path = Path()
        path.moveTo(startX, startY)
        prevOffSet = Offset(startX, startY)
    }

    override fun moveShape(endX: Float, endY: Float) {
        /**
         * Following this answer for SO:
         * https://stackoverflow.com/a/71090112/23198795
         */
        path.quadraticBezierTo(
            x1 = prevOffSet.x,
            y1 = prevOffSet.y,
            x2 = (prevOffSet.x + endX) / 2,
            y2 = (prevOffSet.y + endY) / 2,
        )
        prevOffSet = Offset(endX, endY)
    }

    override fun shouldDraw(): Boolean {
        return true
    }
}