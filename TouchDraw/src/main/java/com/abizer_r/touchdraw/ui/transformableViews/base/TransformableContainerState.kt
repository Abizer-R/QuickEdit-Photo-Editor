package com.abizer_r.touchdraw.ui.transformableViews.base

import com.abizer_r.touchdraw.ui.transformableViews.TransformableViewType

data class TransformableContainerState(
    val transformableViewsList: ArrayList<TransformableViewType> = arrayListOf(),
    val triggerRecomposition: Long = 0
)
