package com.abizer_r.quickedit.utils.drawMode

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import com.abizer_r.quickedit.ui.editorScreen.bottomToolbar.state.BottomToolbarItem
import io.github.abizerr.quickedit.tool.draw.models.DrawToolItem
import io.github.abizerr.quickedit.tool.draw.models.shapes.AbstractShape
import io.github.abizerr.quickedit.tool.draw.models.shapes.BrushShape
import io.github.abizerr.quickedit.tool.draw.models.shapes.LineShape
import io.github.abizerr.quickedit.tool.draw.models.shapes.OvalShape
import io.github.abizerr.quickedit.tool.draw.models.shapes.RectangleShape
import io.github.abizerr.quickedit.engine.drawspec.ShapeType
import io.github.abizerr.quickedit.tool.draw.util.DrawingConstants
import kotlin.math.cos
import kotlin.math.sin

object DrawModeUtils {

    const val DEFAULT_SELECTED_INDEX = 2

    fun getDefaultDrawToolItemsList(): ArrayList<DrawToolItem> {
        return arrayListOf(
            DrawToolItem.ColorItem,
            DrawToolItem.PanItem,
            DrawToolItem.BrushTool(
                width = DrawingConstants.DEFAULT_STROKE_WIDTH,
                opacity = DrawingConstants.DEFAULT_STROKE_OPACITY
            ),
            DrawToolItem.ShapeTool(
                width = DrawingConstants.DEFAULT_STROKE_WIDTH,
                opacity = DrawingConstants.DEFAULT_STROKE_OPACITY,
                shapeType = ShapeType.LINE
            ),
            DrawToolItem.EraserTool(
                width = DrawingConstants.DEFAULT_STROKE_WIDTH
            ),
        )
    }

    fun getDefaultBottomToolbarItemsList(): ArrayList<BottomToolbarItem> {
        return arrayListOf(
            BottomToolbarItem.ColorItem,
            BottomToolbarItem.PanItem,
            BottomToolbarItem.BrushTool(
                width = DrawingConstants.DEFAULT_STROKE_WIDTH,
                opacity = DrawingConstants.DEFAULT_STROKE_OPACITY
            ),
            BottomToolbarItem.ShapeTool(
                width = DrawingConstants.DEFAULT_STROKE_WIDTH,
                opacity = DrawingConstants.DEFAULT_STROKE_OPACITY,
                shapeType = ShapeType.LINE
            ),
            BottomToolbarItem.EraserTool(
                width = DrawingConstants.DEFAULT_STROKE_WIDTH
            ),
        )
    }

    /**
     * This function uses trigonometric functions to compute the new offset values after rotation.
     * new_x = x * cos(θ) - y * sin(θ)
     * new_y = x * sin(θ) + y * cos(θ)
     */
    fun rotateOffset(
        offset: Offset,
        angleDegrees: Float
    ): Offset {
        val angleRadians = Math.toRadians(angleDegrees.toDouble())
        val cosAngle = cos(angleRadians)
        val sinAngle = sin(angleRadians)
        val x = offset.x * cosAngle - offset.y * sinAngle
        val y = offset.x * sinAngle + offset.y * cosAngle
        return Offset(x.toFloat(), y.toFloat())
    }

}

@Composable
fun Dp.toPx(): Float {
    return with(LocalDensity.current) {
        this@toPx.toPx()
    }
}

@Composable
fun Float.pxToDp(): Dp {
    return with(LocalDensity.current) {
        this@pxToDp.toDp()
    }
}