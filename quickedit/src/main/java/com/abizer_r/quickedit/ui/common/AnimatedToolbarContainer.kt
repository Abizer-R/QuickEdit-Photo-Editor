package com.abizer_r.quickedit.ui.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayoutScope
import androidx.constraintlayout.compose.Dimension
import com.abizer_r.quickedit.utils.other.anim.AnimUtils

@Composable
fun AnimatedToolbarContainer(
    toolbarVisible: Boolean,
    modifier: Modifier,
    content: @Composable() AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visible = toolbarVisible,
        modifier = modifier,
        enter = AnimUtils.toolbarExpandAnimFast(),
        exit = AnimUtils.toolbarCollapseAnimFast()
    ) {
        content()
    }
}

@Composable
fun ConstraintLayoutScope.topToolbarModifier(
    constraintRef: ConstrainedLayoutReference,
) = Modifier.constrainAs(constraintRef) {
    top.linkTo(parent.top)
    width = Dimension.matchParent
    height = Dimension.wrapContent
}

@Composable
fun ConstraintLayoutScope.bottomToolbarModifier(
    constraintRef: ConstrainedLayoutReference,
) = Modifier.constrainAs(constraintRef) {
    bottom.linkTo(parent.bottom)
    width = Dimension.matchParent
    height = Dimension.wrapContent
}

@Composable
fun BoxScope.topToolbarModifier() = Modifier
    .fillMaxWidth()
    .align(Alignment.TopCenter)

@Composable
fun BoxScope.bottomToolbarModifier() = Modifier
    .fillMaxWidth()
    .align(Alignment.BottomCenter)