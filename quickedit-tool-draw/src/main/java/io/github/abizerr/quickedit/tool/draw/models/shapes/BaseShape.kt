package io.github.abizerr.quickedit.tool.draw.models.shapes

import android.graphics.Canvas

interface BaseShape {
    fun drawOnAndroidCanvas(canvas: Canvas)
    fun initShape(startX: Float, startY: Float)
    fun moveShape(endX: Float, endY: Float)
    fun shouldDraw(): Boolean
}