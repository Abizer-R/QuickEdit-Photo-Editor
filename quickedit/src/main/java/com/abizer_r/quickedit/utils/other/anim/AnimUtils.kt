package com.abizer_r.quickedit.utils.other.anim

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith

object AnimUtils {

    fun fadeInThenFadeOut(
        fadeInMillis: Int = 200,
        fadeOutMillis: Int = 200
    ) = fadeIn(
        animationSpec = tween(fadeInMillis)
    ) togetherWith fadeOut(
        animationSpec = tween(fadeOutMillis)
    )

    const val TOOLBAR_ANIM_DURATION = 100
    fun toolbarExpandAnim() = fadeIn(
        animationSpec = tween(TOOLBAR_ANIM_DURATION, easing = FastOutLinearInEasing)
    ) + expandIn(
        animationSpec = tween(TOOLBAR_ANIM_DURATION, easing = FastOutLinearInEasing)
    )

    fun toolbarCollapseAnim() = fadeOut(
        animationSpec = tween(TOOLBAR_ANIM_DURATION, easing = FastOutLinearInEasing)
    ) + shrinkOut(
        animationSpec = tween(TOOLBAR_ANIM_DURATION, easing = FastOutLinearInEasing)
    )
}