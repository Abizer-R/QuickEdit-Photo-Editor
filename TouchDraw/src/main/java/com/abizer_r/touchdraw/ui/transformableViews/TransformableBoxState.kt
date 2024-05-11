package com.abizer_r.touchdraw.ui.transformableViews

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp

data class TransformableBoxState(
    val id: String,
    val positionOffset: Offset,
    val viewSizeInDp: Dp,
    val scale: Float,
    val rotation: Float
)
