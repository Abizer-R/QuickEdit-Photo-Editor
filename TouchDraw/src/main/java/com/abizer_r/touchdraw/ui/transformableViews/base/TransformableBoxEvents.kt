package com.abizer_r.touchdraw.ui.transformableViews.base

import androidx.compose.ui.geometry.Offset

sealed class TransformableBoxEvents {
    data class OnDrag(val id: String, val dragAmount: Offset): TransformableBoxEvents()
    data class OnZoom(val id: String, val zoomAmount: Float): TransformableBoxEvents()
    data class OnRotate(val id: String, val rotationChange: Float): TransformableBoxEvents()
//    data class OnTransform(val id: String, val newOffSet: Offset, val newScale: Float, val newRotation: Float): TransformableBoxEvents()
}