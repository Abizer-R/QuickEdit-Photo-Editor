@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.abizer_r.quickedit.ui.effectsMode

import android.content.res.Configuration
import android.graphics.Bitmap
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.abizer_r.components.R
import com.abizer_r.components.theme.QuickEditTheme
import com.abizer_r.components.theme.ToolBarBackgroundColor
import com.abizer_r.components.util.defaultErrorToast
import com.abizer_r.quickedit.ui.editorScreen.bottomToolbar.DEFAULT_TOOLBAR_HEIGHT
import com.abizer_r.quickedit.ui.editorScreen.topToolbar.TextModeTopToolbar
import com.abizer_r.quickedit.ui.effectsMode.effectsPreview.EffectItem
import com.abizer_r.quickedit.ui.effectsMode.effectsPreview.EffectsPreviewListFullWidth
import com.abizer_r.quickedit.utils.SharedTransitionPreviewExtension
import com.abizer_r.quickedit.utils.editorScreen.EffectsModeUtils
import com.abizer_r.quickedit.utils.other.bitmap.ImmutableBitmap
import com.smarttoolfactory.screenshot.ImageResult
import com.smarttoolfactory.screenshot.ScreenshotBox
import com.smarttoolfactory.screenshot.rememberScreenshotState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SharedTransitionScope.EffectsModeScreen(
    modifier: Modifier = Modifier,
    animatedVisibilityScope: AnimatedVisibilityScope,
    immutableBitmap: ImmutableBitmap,
    onDoneClicked: (bitmap: Bitmap) -> Unit,
    onBackPressed: () -> Unit
) {
    val context = LocalContext.current
    val lifeCycleOwner = LocalLifecycleOwner.current

    val viewModel: EffectsModeViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle(
        lifecycleOwner = lifeCycleOwner
    )

    val bitmap = immutableBitmap.bitmap
    val currentBitmap = state.filteredBitmap ?: bitmap

    BackHandler {
        onBackPressed()
    }

    LaunchedEffect(key1 = bitmap) {
        withContext(Dispatchers.IO) {

            EffectsModeUtils.getEffectsPreviewList(context, bitmap).onEach {
                viewModel.addToEffectList(
                    effectItems = it,
                    selectInitialBitmap = state.effectsList.isEmpty(),
                )
            }.collect()

//            val mEffectList = EffectsModeUtils.getEffectsPreviewList(context, bitmap)
//            viewModel.updateEffectList(
//                effectList = mEffectList
//            )
//            viewModel.selectEffect(0)
        }
    }

    val screenshotState = rememberScreenshotState()

    when (screenshotState.imageState.value) {
        ImageResult.Initial -> {}
        is ImageResult.Error -> {
            viewModel.shouldGoToNextScreen = false
            context.defaultErrorToast()
        }

        is ImageResult.Success -> {
            if (viewModel.shouldGoToNextScreen) {
                viewModel.shouldGoToNextScreen = false
                screenshotState.bitmap?.let { mBitmap ->
                    onDoneClicked(mBitmap)
                } ?: context.defaultErrorToast()
            }
        }
    }

    val onCloseClickedLambda = remember<() -> Unit> { {
        onBackPressed()
    }}

    val onDoneClickedLambda = remember<() -> Unit> { {
        viewModel.shouldGoToNextScreen = true
        screenshotState.capture()
    }}


    val onEffectItemClicked = remember<(Int, EffectItem) -> Unit> {{ index, effectItem ->
        viewModel.selectEffect(index)
    }}


    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .imePadding()
    ) {
        val (topToolBar, screenshotBox, effectsPreviewList) = createRefs()

        TextModeTopToolbar(
            modifier = Modifier.constrainAs(topToolBar) {
                top.linkTo(parent.top)
                width = Dimension.matchParent
                height = Dimension.wrapContent
            },
            onCloseClicked = onCloseClickedLambda,
            onDoneClicked = onDoneClickedLambda
        )
        val aspectRatio = bitmap.let {
            bitmap.width.toFloat() / bitmap.height.toFloat()
        }
        ScreenshotBox(
            modifier = Modifier
                .constrainAs(screenshotBox) {
                    top.linkTo(topToolBar.bottom)
                    bottom.linkTo(effectsPreviewList.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.ratio(aspectRatio.toString())
                    height = Dimension.fillToConstraints
                }
                .clipToBounds()
                .animateContentSize()
                .sharedElement(
                    state = rememberSharedContentState(key = "centerImage"),
                    animatedVisibilityScope = animatedVisibilityScope,
                    boundsTransform = { _, _ ->
                        tween(300)
                    },
                ),
            screenshotState = screenshotState
        ) {

            Image(
                modifier = Modifier.fillMaxSize(),
                bitmap = currentBitmap.asImageBitmap(),
                contentDescription = null
            )

        }


        Box(
            modifier = Modifier
                .constrainAs(effectsPreviewList) {
                    bottom.linkTo(parent.bottom)
                    width = Dimension.matchParent
                    height = if (state.effectsList.isEmpty()) {
                        Dimension.value(DEFAULT_TOOLBAR_HEIGHT)
                    } else Dimension.wrapContent
                }
                .background(ToolBarBackgroundColor)
                .animateContentSize()
                .sharedElement(
                    state = rememberSharedContentState(key = "bottomToolbar"),
                    animatedVisibilityScope = animatedVisibilityScope,
                ),
        ) {

            if (state.effectsList.isEmpty()) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(36.dp)
                        .align(Alignment.Center),
                    color = MaterialTheme.colorScheme.onBackground,
                    strokeWidth = 3.dp,
                )
            } else {
                EffectsPreviewListFullWidth(
                    modifier = Modifier
                        .background(ToolBarBackgroundColor)
                        .padding(vertical = 12.dp),
                    effectsList = state.effectsList,
                    selectedIndex = state.selectedEffectIndex,
                    onItemClicked = onEffectItemClicked
                )
            }

        }

    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun Preview_EffectsModeScreen() {
    QuickEditTheme {
        SharedTransitionPreviewExtension {
            EffectsModeScreen(
                immutableBitmap = ImmutableBitmap(
                    ImageBitmap.imageResource(id = R.drawable.placeholder_image_2).asAndroidBitmap()
                ),
                animatedVisibilityScope = it,
                onDoneClicked = {},
                onBackPressed = {}
            )

        }
    }
}