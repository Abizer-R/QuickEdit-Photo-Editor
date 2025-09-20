package io.github.abizerr.quickedit.tool.draw.models.shapes

import androidx.compose.ui.graphics.drawscope.DrawScope

interface BaseShape {
    fun draw(drawScope: DrawScope)
    fun initShape(startX: Float, startY: Float)
    fun moveShape(endX: Float, endY: Float)
    fun shouldDraw(): Boolean
}