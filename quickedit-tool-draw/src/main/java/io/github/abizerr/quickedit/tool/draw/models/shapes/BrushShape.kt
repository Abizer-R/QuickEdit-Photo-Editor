package io.github.abizerr.quickedit.tool.draw.models.shapes

import android.graphics.Canvas
import android.graphics.Paint
import io.github.abizerr.quickedit.engine.drawspec.ShapeSpec
import io.github.abizerr.quickedit.engine.drawspec.toPaintSpec
import io.github.abizerr.quickedit.engine.util.applySpec
import io.github.abizerr.quickedit.engine.util.drawOn

class BrushShape(
    private val shapeSpec: ShapeSpec.Brush
): BaseShape {

    override fun drawOnAndroidCanvas(canvas: Canvas) {
        val paintSpec = shapeSpec.toPaintSpec()
        val paint = Paint().applySpec(paintSpec)
        shapeSpec.drawOn(canvas, paint)
    }

    override fun initShape(startX: Float, startY: Float) {
        shapeSpec.points.add(Pair(startX, startY))
    }

    override fun moveShape(endX: Float, endY: Float) {
        shapeSpec.points.add(Pair(endX, endY))
    }

    override fun shouldDraw(): Boolean {
        return true
    }
}