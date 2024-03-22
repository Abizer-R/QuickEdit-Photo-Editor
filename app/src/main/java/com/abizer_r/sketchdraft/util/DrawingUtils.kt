package com.abizer_r.sketchdraft.util

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

object DrawingUtils {
}

fun DrawScope.drawDefaultPath(
    path: Path,
    strokeColor: Color,
    strokeWidth: Float,
    alpha: Float
) {
    drawPath(
        path = path,
        brush = SolidColor(strokeColor),
        style = Stroke(
            width = strokeWidth,
            cap = StrokeCap.Round
        ),
        alpha = alpha
    )
}