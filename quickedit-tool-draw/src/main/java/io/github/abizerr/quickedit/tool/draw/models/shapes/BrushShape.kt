package io.github.abizerr.quickedit.tool.draw.models.shapes

import android.graphics.Canvas
import android.graphics.Paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb
import io.github.abizerr.quickedit.engine.drawspec.PaintSpec
import io.github.abizerr.quickedit.engine.drawspec.ShapeSpec
import io.github.abizerr.quickedit.engine.drawspec.toPaintSpec
import io.github.abizerr.quickedit.engine.util.applySpec
import io.github.abizerr.quickedit.engine.util.drawOn

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

    private val points = arrayListOf<Pair<Float, Float>>()

    override fun drawOnAndroidCanvas(canvas: Canvas) {
        val shapeSpec = ShapeSpec.Brush(
            points = points,
            argb = mColor.toArgb(),
            alpha = mAlpha,
            widthPx = mWidth,
            isEraser = isEraser
        )
        val paintSpec = shapeSpec.toPaintSpec()
        val paint = Paint().applySpec(paintSpec)
        shapeSpec.drawOn(canvas, paint)
    }

    override fun initShape(startX: Float, startY: Float) {
        path = Path()
        path.moveTo(startX, startY)
        prevOffSet = Offset(startX, startY)
        points.add(Pair(startX, startY))
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
        points.add(Pair(endX, endY))
    }

    override fun shouldDraw(): Boolean {
        return true
    }
}