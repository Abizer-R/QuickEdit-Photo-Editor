package com.abizer_r.touchdraw.ui.transformableViews.base

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import com.abizer_r.touchdraw.ui.transformableViews.TransformableViewType

data class TransformableBoxState(
    val id: String,
    var positionOffset: Offset = Offset(0f, 0f),
    var scale: Float = 1f,
    var rotation: Float = 0f,
    var isSelected: Boolean = true
)
