package io.github.abizerr.quickedit.ui.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayoutScope
import androidx.constraintlayout.compose.Dimension
import io.github.abizerr.quickedit.ui.utils.anim.AnimUtils

val TOOLBAR_HEIGHT_SMALL = 48.dp
val TOOLBAR_HEIGHT_MEDIUM = 64.dp
val TOOLBAR_HEIGHT_LARGE = 88.dp
val TOOLBAR_HEIGHT_XL = 104.dp

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
) = Modifier.Companion.constrainAs(constraintRef) {
    top.linkTo(parent.top)
    width = Dimension.matchParent
    height = Dimension.wrapContent
}

@Composable
fun ConstraintLayoutScope.bottomToolbarModifier(
    constraintRef: ConstrainedLayoutReference,
) = Modifier.Companion.constrainAs(constraintRef) {
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