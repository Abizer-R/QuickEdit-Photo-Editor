package io.github.abizerr.quickedit.tool.draw.models.shapes

import android.graphics.Canvas
import android.graphics.Paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb
import io.github.abizerr.quickedit.engine.drawspec.ShapeSpec
import io.github.abizerr.quickedit.engine.drawspec.ShapeType
import io.github.abizerr.quickedit.engine.drawspec.toPaintSpec
import io.github.abizerr.quickedit.engine.util.applySpec
import io.github.abizerr.quickedit.engine.util.drawOn

class OvalShape(
    color: Color? = null,
    width: Float? = null,
    alpha: Float? = null
) : AbstractShape() {

    init {
        updatePaintValues(color, width, alpha)
    }

    private var startOffset: Offset = Offset.Unspecified
    private var endOffset: Offset = Offset.Unspecified

    override fun drawOnAndroidCanvas(canvas: Canvas) {
        val shapeSpec = ShapeSpec.Shape(
            shapeType = ShapeType.OVAL,
            startOffset = Pair(startOffset.x, startOffset.y),
            endOffset = Pair(endOffset.x, endOffset.y),
            argb = mColor.toArgb(),
            alpha = mAlpha,
            widthPx = mWidth,
        )
        val paintSpec = shapeSpec.toPaintSpec()
        val paint = Paint().applySpec(paintSpec)
        shapeSpec.drawOn(canvas, paint)
    }

    override fun initShape(startX: Float, startY: Float) {
        startOffset = Offset(startX, startY)
    }

    override fun moveShape(endX: Float, endY: Float) {
        endOffset = Offset(endX, endY)
    }

    override fun shouldDraw(): Boolean {
        return startOffset != Offset.Unspecified && endOffset != Offset.Unspecified
    }
}