package io.github.abizerr.quickedit.engine.util

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import io.github.abizerr.quickedit.engine.drawspec.PaintSpec
import io.github.abizerr.quickedit.engine.drawspec.ShapeSpec
import io.github.abizerr.quickedit.engine.drawspec.ShapeType
import io.github.abizerr.quickedit.engine.drawspec.toPaintSpec

fun Paint.applySpec(paintSpec: PaintSpec): Paint = this.apply {
    isAntiAlias = true
    style = Paint.Style.STROKE
    strokeCap = Paint.Cap.ROUND
    strokeJoin = Paint.Join.ROUND
    strokeWidth = paintSpec.widthPx
    color = paintSpec.argb
    alpha = (paintSpec.alpha * 255f).toInt()
    xfermode = if (paintSpec.isEraser) PorterDuffXfermode(PorterDuff.Mode.CLEAR) else null
}

fun ShapeSpec.drawOn(canvas: Canvas, paint: Paint) {
    val ps = toPaintSpec()
    paint.applySpec(ps)
    when (this) {
        is ShapeSpec.Brush -> {
            val path = android.graphics.Path()
            var last: Pair<Float, Float>? = null
            for (p in points) {
                if (last == null) {
                    path.moveTo(p.first, p.second)
                } else {
                    path.quadTo(
                        last.first,
                        last.second,
                        (last.first + p.first) / 2,
                        (last.second + p.second) / 2,
                    )
                }
                last = p
            }
            canvas.drawPath(path, paint)
        }

        is ShapeSpec.Shape -> {
            when (this.shapeType) {
                ShapeType.LINE -> canvas.drawLine(
                    startOffset.first,
                    startOffset.second,
                    endOffset.first,
                    endOffset.second,
                    paint
                )

                ShapeType.OVAL -> canvas.drawOval(
                    startOffset.first,
                    startOffset.second,
                    endOffset.first,
                    endOffset.second,
                    paint
                )
                ShapeType.RECTANGLE -> canvas.drawRect(
                    startOffset.first,
                    startOffset.second,
                    endOffset.first,
                    endOffset.second,
                    paint
                )
            }
        }
    }
}