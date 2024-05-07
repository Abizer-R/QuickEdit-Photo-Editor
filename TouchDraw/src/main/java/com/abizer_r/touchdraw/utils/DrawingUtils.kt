package com.abizer_r.touchdraw.utils

import androidx.compose.ui.graphics.Color
import com.abizer_r.touchdraw.ui.drawingCanvas.DrawingState
import com.abizer_r.touchdraw.ui.drawingCanvas.models.PaintValues
import com.abizer_r.touchdraw.ui.drawingCanvas.drawingTool.shapes.BrushShape
import com.abizer_r.touchdraw.ui.drawingCanvas.drawingTool.shapes.AbstractShape
import com.abizer_r.touchdraw.ui.drawingCanvas.drawingTool.shapes.LineShape
import com.abizer_r.touchdraw.ui.drawingCanvas.drawingTool.shapes.OvalShape
import com.abizer_r.touchdraw.ui.drawingCanvas.drawingTool.shapes.RectangleShape
import com.abizer_r.touchdraw.ui.drawingCanvas.drawingTool.shapes.ShapeTypes
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.state.BottomToolbarItem
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.state.BottomToolbarState

object DrawingUtils {

    const val TOUCH_TOLERANCE = 4f

    fun getDefaultBottomToolbarItemsList(
        defaultColorSelected: Color = Color.White
    ): ArrayList<BottomToolbarItem> {
        return arrayListOf(
            BottomToolbarItem.ColorItem(currentColor = defaultColorSelected),
            BottomToolbarItem.BrushTool(
                width = 12, opacity = 100
            ),
            BottomToolbarItem.ShapeTool(
                width = 12, opacity = 100, shapeTypes = ShapeTypes.LINE
            ),
            BottomToolbarItem.EraserTool(
                width = 12
            )
        )
    }

    fun getDefaultBottomToolbarState(
        defaultColorSelected: Color = Color.White
    ): BottomToolbarState {
        val toolbarListItems = getDefaultBottomToolbarItemsList(defaultColorSelected)
        return BottomToolbarState(
            toolbarItems = toolbarListItems,
            selectedItem = toolbarListItems[1],
            selectedColor = defaultColorSelected
        )
    }
}

fun BottomToolbarItem.getShape(
    selectedColor: Color
): AbstractShape {
    return when (val toolbarItem = this) {
        is BottomToolbarItem.BrushTool -> {
            BrushShape(
                color = selectedColor,
                width = toolbarItem.width.toFloat(),
                alpha = toolbarItem.opacity.toFloat() / 100f
            )
        }

        is BottomToolbarItem.ShapeTool -> when (toolbarItem.shapeTypes) {
            ShapeTypes.LINE -> LineShape(
                color = selectedColor,
                width = toolbarItem.width.toFloat(),
                alpha = toolbarItem.opacity.toFloat() / 100f
            )

            ShapeTypes.OVAL -> OvalShape(
                color = selectedColor,
                width = toolbarItem.width.toFloat(),
                alpha = toolbarItem.opacity.toFloat() / 100f
            )

            ShapeTypes.RECTANGLE -> RectangleShape(
                color = selectedColor,
                width = toolbarItem.width.toFloat(),
                alpha = toolbarItem.opacity.toFloat() / 100f
            )
        }

        else -> {
            /**
             * this else block represents the EraserTool, as ColorItem won't be sent here
             */
            val width =
                if (toolbarItem is BottomToolbarItem.EraserTool) toolbarItem.width.toFloat()
                else DrawingConstants.DEFAULT_STROKE_WIDTH
            BrushShape(
                isEraser = true,
                width = width
            )
        }
    }
}