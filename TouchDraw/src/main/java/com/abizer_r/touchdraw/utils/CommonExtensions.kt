package com.abizer_r.touchdraw.utils

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable

val Any.TAG: String
    get() {
        val tag = javaClass.simpleName
        return if (tag.length <= 23) tag else tag.substring(0, 23)
    }

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionPreviewExtension(
    content: @Composable SharedTransitionScope.(AnimatedVisibilityScope) -> Unit
) {
    SharedTransitionLayout {
        AnimatedVisibility(visible = true) {
            content(this)
        }
    }
}