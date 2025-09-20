package io.github.abizerr.quickedit.tool.draw.models

import androidx.compose.ui.graphics.Color
import io.github.abizerr.quickedit.tool.draw.models.shapes.AbstractShape
import io.github.abizerr.quickedit.tool.draw.models.shapes.BrushShape
import io.github.abizerr.quickedit.tool.draw.models.shapes.LineShape
import io.github.abizerr.quickedit.tool.draw.models.shapes.OvalShape
import io.github.abizerr.quickedit.tool.draw.models.shapes.RectangleShape
import io.github.abizerr.quickedit.tool.draw.models.shapes.ShapeType
import io.github.abizerr.quickedit.tool.draw.util.DrawingConstants

sealed class DrawToolItem {
    object NONE: DrawToolItem()
    object ColorItem : DrawToolItem()
    object PanItem : DrawToolItem()
    class BrushTool(var width: Float, var opacity: Float) : DrawToolItem()
    class ShapeTool(var width: Float, var opacity: Float, var shapeType: ShapeType) :
        DrawToolItem()
    class EraserTool(var width: Float) : DrawToolItem()
}

fun DrawToolItem.getShape(
    selectedColor: Color,
    scale: Float = 1f,
): AbstractShape {
    return when (val toolbarItem = this) {
        is DrawToolItem.BrushTool -> {
            BrushShape(
                color = selectedColor,
                width = toolbarItem.width / scale,
                alpha = toolbarItem.opacity / 100f
            )
        }

        is DrawToolItem.ShapeTool -> when (toolbarItem.shapeType) {
            ShapeType.LINE -> LineShape(
                color = selectedColor,
                width = toolbarItem.width / scale,
                alpha = toolbarItem.opacity / 100f
            )

            ShapeType.OVAL -> OvalShape(
                color = selectedColor,
                width = toolbarItem.width / scale,
                alpha = toolbarItem.opacity / 100f
            )

            ShapeType.RECTANGLE -> RectangleShape(
                color = selectedColor,
                width = toolbarItem.width / scale,
                alpha = toolbarItem.opacity / 100f
            )
        }

        else -> {
            /**
             * this else block represents the EraserTool, as any other item (ColorItem, PanItem) won't be sent here
             */
            val width =
                if (toolbarItem is DrawToolItem.EraserTool) toolbarItem.width
                else DrawingConstants.DEFAULT_STROKE_WIDTH
            BrushShape(
                isEraser = true,
                width = width / scale
            )
        }
    }
}

fun DrawToolItem.getWidthOrNull(): Float? {
    return when (this) {
        is DrawToolItem.BrushTool -> this.width
        is DrawToolItem.EraserTool -> this.width
        is DrawToolItem.ShapeTool -> this.width
        else -> null
    }
}

fun DrawToolItem.setWidthIfPossible(mWidth: Float): DrawToolItem {
    when (this) {
        is DrawToolItem.BrushTool -> this.width = mWidth
        is DrawToolItem.EraserTool -> this.width = mWidth
        is DrawToolItem.ShapeTool -> this.width = mWidth
        else -> {}
    }
    return this
}

fun DrawToolItem.getOpacityOrNull(): Float? {
    return when (this) {
        is DrawToolItem.BrushTool -> this.opacity
        is DrawToolItem.ShapeTool -> this.opacity
        else -> null
    }
}

fun DrawToolItem.setOpacityIfPossible(mOpacity: Float): DrawToolItem {
    when (this) {
        is DrawToolItem.BrushTool -> this.opacity = mOpacity
        is DrawToolItem.ShapeTool -> this.opacity = mOpacity
        else -> {}
    }
    return this
}

fun DrawToolItem.getShapeTypeOrNull(): ShapeType? {
    return when (this) {
        is DrawToolItem.ShapeTool -> this.shapeType
        else -> null
    }
}

fun DrawToolItem.setShapeTypeIfPossible(mShapeType: ShapeType): DrawToolItem {
    when (this) {
        is DrawToolItem.ShapeTool -> this.shapeType = mShapeType
        else -> {}
    }
    return this
}