package io.github.abizerr.quickedit.tool.draw.models.shapes

import android.graphics.Canvas
import android.graphics.Paint
import io.github.abizerr.quickedit.engine.drawspec.ShapeSpec
import io.github.abizerr.quickedit.engine.drawspec.toPaintSpec
import io.github.abizerr.quickedit.engine.util.applySpec
import io.github.abizerr.quickedit.engine.util.drawOn

class LineShape(
    private val shapeSpec: ShapeSpec.Shape
) : BaseShape {

    override fun drawOnAndroidCanvas(canvas: Canvas) {
        val paintSpec = shapeSpec.toPaintSpec()
        val paint = Paint().applySpec(paintSpec)
        shapeSpec.drawOn(canvas, paint)
    }

    override fun initShape(startX: Float, startY: Float) {
        shapeSpec.startOffset = Pair(startX, startY)
    }

    override fun moveShape(endX: Float, endY: Float) {
        shapeSpec.endOffset = Pair(endX, endY)
    }

    override fun shouldDraw(): Boolean {
        return shapeSpec.startOffset != ShapeSpec.defaultOffset
                && shapeSpec.endOffset != ShapeSpec.defaultOffset
    }
}