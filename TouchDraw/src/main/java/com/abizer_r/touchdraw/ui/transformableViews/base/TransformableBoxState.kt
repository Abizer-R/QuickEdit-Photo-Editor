package com.abizer_r.touchdraw.ui.transformableViews.base

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import com.abizer_r.touchdraw.ui.transformableViews.TransformableViewType

data class TransformableBoxState(
    val id: String,
    var positionOffset: Offset,
    var scale: Float,
    var rotation: Float
)
