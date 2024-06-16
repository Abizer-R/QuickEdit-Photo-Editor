package com.abizer_r.touchdraw.utils.drawMode

import android.graphics.RectF
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.core.graphics.transform
import com.abizer_r.touchdraw.ui.drawMode.drawingCanvas.drawingTool.shapes.BrushShape
import com.abizer_r.touchdraw.ui.drawMode.drawingCanvas.drawingTool.shapes.AbstractShape
import com.abizer_r.touchdraw.ui.drawMode.drawingCanvas.drawingTool.shapes.LineShape
import com.abizer_r.touchdraw.ui.drawMode.drawingCanvas.drawingTool.shapes.OvalShape
import com.abizer_r.touchdraw.ui.drawMode.drawingCanvas.drawingTool.shapes.RectangleShape
import com.abizer_r.touchdraw.ui.drawMode.drawingCanvas.drawingTool.shapes.ShapeType
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.state.BottomToolbarItem
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.state.BottomToolbarState
import kotlin.math.cos
import kotlin.math.sin

object DrawModeUtils {

    fun getDefaultBottomToolbarState(
        defaultColorSelected: Color = Color.White
    ): BottomToolbarState {
        val toolbarListItems = getDefaultBottomToolbarItemsList()
        return BottomToolbarState(
            toolbarItems = toolbarListItems,
            selectedItem = toolbarListItems[1],
            selectedColor = defaultColorSelected,
            showColorPickerIcon = true
        )
    }

    private fun getDefaultBottomToolbarItemsList(): ArrayList<BottomToolbarItem> {
        return arrayListOf(
            BottomToolbarItem.ColorItem,
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
//            BottomToolbarItem.TextMode
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

fun BottomToolbarItem.getShape(
    selectedColor: Color
): AbstractShape {
    return when (val toolbarItem = this) {
        is BottomToolbarItem.BrushTool -> {
            BrushShape(
                color = selectedColor,
                width = toolbarItem.width,
                alpha = toolbarItem.opacity / 100f
            )
        }

        is BottomToolbarItem.ShapeTool -> when (toolbarItem.shapeType) {
            ShapeType.LINE -> LineShape(
                color = selectedColor,
                width = toolbarItem.width,
                alpha = toolbarItem.opacity / 100f
            )

            ShapeType.OVAL -> OvalShape(
                color = selectedColor,
                width = toolbarItem.width,
                alpha = toolbarItem.opacity / 100f
            )

            ShapeType.RECTANGLE -> RectangleShape(
                color = selectedColor,
                width = toolbarItem.width,
                alpha = toolbarItem.opacity / 100f
            )
        }

        else -> {
            /**
             * this else block represents the EraserTool, as ColorItem won't be sent here
             */
            val width =
                if (toolbarItem is BottomToolbarItem.EraserTool) toolbarItem.width
                else DrawingConstants.DEFAULT_STROKE_WIDTH
            BrushShape(
                isEraser = true,
                width = width
            )
        }
    }
}

fun BottomToolbarItem.getWidthOrNull(): Float? {
    return when (this) {
        is BottomToolbarItem.BrushTool -> this.width
        is BottomToolbarItem.EraserTool -> this.width
        is BottomToolbarItem.ShapeTool -> this.width
        else -> null
    }
}

fun BottomToolbarItem.setWidthIfPossible(mWidth: Float): BottomToolbarItem {
    when (this) {
        is BottomToolbarItem.BrushTool -> this.width = mWidth
        is BottomToolbarItem.EraserTool -> this.width = mWidth
        is BottomToolbarItem.ShapeTool -> this.width = mWidth
        else -> {}
    }
    return this
}

fun BottomToolbarItem.getOpacityOrNull(): Float? {
    return when (this) {
        is BottomToolbarItem.BrushTool -> this.opacity
        is BottomToolbarItem.ShapeTool -> this.opacity
        else -> null
    }
}

fun BottomToolbarItem.setOpacityIfPossible(mOpacity: Float): BottomToolbarItem {
    when (this) {
        is BottomToolbarItem.BrushTool -> this.opacity = mOpacity
        is BottomToolbarItem.ShapeTool -> this.opacity = mOpacity
        else -> {}
    }
    return this
}

fun BottomToolbarItem.getShapeTypeOrNull(): ShapeType? {
    return when (this) {
        is BottomToolbarItem.ShapeTool -> this.shapeType
        else -> null
    }
}

fun BottomToolbarItem.setShapeTypeIfPossible(mShapeType: ShapeType): BottomToolbarItem {
    when (this) {
        is BottomToolbarItem.ShapeTool -> this.shapeType = mShapeType
        else -> {}
    }
    return this
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