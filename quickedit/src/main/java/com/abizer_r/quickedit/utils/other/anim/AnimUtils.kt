package com.abizer_r.quickedit.utils.other.anim

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry

object AnimUtils {

    val EMPHASIZED_DECELERATE = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1.0f)
    val EMPHASIZED_ACCELERATE = CubicBezierEasing(0.3f, 0f, 0.8f, 0.15f)

    const val TOOLBAR_EXPAND_ANIM_DURATION = 400
    const val TOOLBAR_COLLAPSE_ANIM_DURATION = 250

    const val TOOLBAR_EXPAND_ANIM_DURATION_FAST = 200
    const val TOOLBAR_COLLAPSE_ANIM_DURATION_FAST = 200

    fun fadeInThenFadeOut(
        fadeInMillis: Int = 200,
        fadeOutMillis: Int = 200
    ) = fadeIn(
        animationSpec = tween(fadeInMillis)
    ) togetherWith fadeOut(
        animationSpec = tween(fadeOutMillis)
    )

    fun toolbarExtensionExpandAnim() = fadeIn(
        animationSpec = tween(TOOLBAR_EXPAND_ANIM_DURATION, easing = EMPHASIZED_DECELERATE)
    ) + expandIn(
        animationSpec = tween(TOOLBAR_EXPAND_ANIM_DURATION, easing = EMPHASIZED_DECELERATE)
    )

    fun toolbarExtensionCollapseAnim() = fadeOut(
        animationSpec = tween(TOOLBAR_COLLAPSE_ANIM_DURATION, easing = EMPHASIZED_ACCELERATE)
    ) + shrinkOut(
        animationSpec = tween(TOOLBAR_COLLAPSE_ANIM_DURATION, easing = EMPHASIZED_ACCELERATE)
    )

    fun toolbarExpandAnimFast() = expandIn(
        animationSpec = tween(TOOLBAR_EXPAND_ANIM_DURATION_FAST, easing = EMPHASIZED_DECELERATE)
    )

    fun toolbarCollapseAnimFast() = shrinkOut(
        animationSpec = tween(TOOLBAR_COLLAPSE_ANIM_DURATION_FAST, easing = EMPHASIZED_ACCELERATE)
    )
}


fun AnimatedContentTransitionScope<NavBackStackEntry>.getDefaultEnterTransition(): EnterTransition {
    return slideIntoContainer(
        AnimatedContentTransitionScope.SlideDirection.Start, tween(500, easing = LinearOutSlowInEasing)
    )
}

fun AnimatedContentTransitionScope<NavBackStackEntry>.getDefaultExitTransition(): ExitTransition {
    return slideOutOfContainer(
        AnimatedContentTransitionScope.SlideDirection.End, tween(500, easing = FastOutSlowInEasing)
    )
}