package io.github.abizerr.quickedit.tool.draw.models.shapes

import androidx.compose.ui.graphics.Color
import io.github.abizerr.quickedit.engine.drawspec.ShapeSpec
import io.github.abizerr.quickedit.tool.draw.util.DrawingConstants

abstract class AbstractShape: BaseShape {
    abstract var shapeSpec: ShapeSpec
//    var mColor: Color = Color.White
//    var mWidth: Float = DrawingConstants.DEFAULT_STROKE_WIDTH
//    var mAlpha: Float = DrawingConstants.DEFAULT_STROKE_ALPHA

}