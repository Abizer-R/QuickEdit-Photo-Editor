package com.abizer_r.quickedit.ui.editorScreen

import android.content.res.Configuration
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.abizer_r.quickedit.R
import com.abizer_r.quickedit.theme.QuickEditTheme
import com.abizer_r.quickedit.utils.ImmutableList
import com.abizer_r.quickedit.ui.common.AnimatedToolbarContainer
import com.abizer_r.quickedit.ui.common.bottomToolbarModifier
import com.abizer_r.quickedit.ui.common.topToolbarModifier
import com.abizer_r.quickedit.ui.drawMode.stateHandling.DrawModeEvent
import com.abizer_r.quickedit.ui.editorScreen.bottomToolbar.BottomToolBarStatic
import com.abizer_r.quickedit.ui.editorScreen.bottomToolbar.TOOLBAR_HEIGHT_MEDIUM
import com.abizer_r.quickedit.ui.editorScreen.bottomToolbar.TOOLBAR_HEIGHT_SMALL
import com.abizer_r.quickedit.ui.editorScreen.bottomToolbar.state.BottomToolbarEvent
import com.abizer_r.quickedit.ui.editorScreen.bottomToolbar.state.BottomToolbarItem
import com.abizer_r.quickedit.ui.editorScreen.topToolbar.TopToolBar
import com.abizer_r.quickedit.utils.FileUtils
import com.abizer_r.quickedit.utils.editorScreen.EditorScreenUtils
import com.abizer_r.quickedit.utils.other.anim.AnimUtils
import com.abizer_r.quickedit.utils.other.bitmap.BitmapUtils
import com.abizer_r.quickedit.utils.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun EditorScreen(
    modifier: Modifier = Modifier,
    initialEditorScreenState: EditorScreenState,
    goToCropModeScreen: (finalEditorState: EditorScreenState) -> Unit,
    goToDrawModeScreen: (finalEditorState: EditorScreenState) -> Unit,
    goToTextModeScreen: (finalEditorState: EditorScreenState) -> Unit,
    goToEffectsModeScreen: (finalEditorState: EditorScreenState) -> Unit,
    goToMainScreen: () -> Unit,
) {
    if (initialEditorScreenState.bitmapStack.isEmpty()) {
        throw Exception("EmptyStackException: The bitmapStack of initial state should contain at least one bitmap")
    }

    val context = LocalContext.current
    val lifeCycleOwner = LocalLifecycleOwner.current

    val viewModel: EditorScreenViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle(
        lifecycleOwner = lifeCycleOwner
    )

    LaunchedEffect(key1 = Unit) {
        viewModel.updateInitialState(initialEditorScreenState)
    }

    if (state.bitmapStack.isNotEmpty()) {
        // Adding this check because the default state in viewModel will have empty stack
        // After updating the initialEditorScreenState, we will have non-empty stack
        val currentBitmap = viewModel.getCurrentBitmap()

        val onBottomToolbarEvent = remember<(BottomToolbarEvent) -> Unit> {{ toolbarEvent ->
            if (toolbarEvent is BottomToolbarEvent.OnItemClicked) {
                when (toolbarEvent.toolbarItem) {
                    BottomToolbarItem.CropMode -> {
                        goToCropModeScreen(state)
                    }
                    BottomToolbarItem.DrawMode -> {
                        goToDrawModeScreen(state)
                    }
                    BottomToolbarItem.TextMode -> {
                        goToTextModeScreen(state)
                    }
                    BottomToolbarItem.EffectsMode -> {
                        goToEffectsModeScreen(state)
                    }
                    else -> {}
                }
            }
        }}

        EditorScreenLayout(
            modifier = modifier,
            currentBitmap = currentBitmap,
            undoEnabled = viewModel.undoEnabled(),
            redoEnabled = viewModel.redoEnabled(),
            onUndo = viewModel::onUndo,
            onRedo = viewModel::onRedo,
            onBottomToolbarEvent = onBottomToolbarEvent,
            goToMainScreen = goToMainScreen
        )
    }

}

@Composable
private fun EditorScreenLayout(
    modifier: Modifier,
    currentBitmap: Bitmap,
    undoEnabled: Boolean,
    redoEnabled: Boolean,
    onUndo: () -> Unit,
    onRedo: () -> Unit,
    onBottomToolbarEvent: (BottomToolbarEvent) -> Unit,
    goToMainScreen: () -> Unit,
) {

    val context = LocalContext.current
    val lifeCycleOwner = LocalLifecycleOwner.current

    val bottomToolbarItems = remember {
        ImmutableList(EditorScreenUtils.getDefaultBottomToolbarItemsList())
    }

    val topToolbarHeight =  TOOLBAR_HEIGHT_SMALL
    val bottomToolbarHeight = TOOLBAR_HEIGHT_MEDIUM

    var toolbarVisible by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = Unit) { toolbarVisible = true }

    val mOnBottomToolbarEvent = remember<(BottomToolbarEvent) -> Unit> {{ toolbarEvent ->
        if (toolbarEvent is BottomToolbarEvent.OnItemClicked) {
            lifeCycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                toolbarVisible = false
                delay(AnimUtils.TOOLBAR_COLLAPSE_ANIM_DURATION_FAST.toLong())
                onBottomToolbarEvent(toolbarEvent)
            }
        }
    }}

    val onCloseClickedLambda = remember<() -> Unit> {{
        // TODO - confirmation dialog (add throughout the app)
        goToMainScreen()
    }}

    val onDoneClickedLambda = remember<() -> Unit> {{
        // TODO - confirmation dialog before saving image

        // TODO - show loading dialog or something

        context.toast(R.string.saving_image)
        val imgFile = File(context.filesDir, "edited_image.jpg")
        BitmapUtils.saveBitmap(currentBitmap, imgFile)
        FileUtils.saveFileToAppFolder(
            context = context,
            file = imgFile,
            onSuccess = {
                context.toast(R.string.image_saved_successfully)
                goToMainScreen()
            },
            onFailure = {
                context.toast(R.string.something_went_wrong)
            },
        )
    }}

    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val (topToolbar, bottomToolbar, bgImage) = createRefs()

        AnimatedToolbarContainer(
            toolbarVisible = toolbarVisible,
            modifier = topToolbarModifier(topToolbar)
        ) {
            TopToolBar(
                modifier = Modifier,
                undoEnabled = undoEnabled,
                redoEnabled = redoEnabled,
                toolbarHeight = topToolbarHeight,
                showCloseAndDone = true,
                doneEnabled = undoEnabled,
                onUndo = onUndo,
                onRedo = onRedo,
                onCloseClicked = onCloseClickedLambda,
                onDoneClicked = onDoneClickedLambda
            )
        }

        val aspectRatio = remember(currentBitmap) {
            currentBitmap.width.toFloat() / currentBitmap.height.toFloat()
        }
        Box(
            modifier = Modifier
                .constrainAs(bgImage) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.wrapContent
                    height = Dimension.wrapContent
                }
                .padding(top = topToolbarHeight, bottom = bottomToolbarHeight)
                .aspectRatio(aspectRatio),
        ) {

            Image(
                modifier = Modifier
                    .fillMaxSize(),
                bitmap = currentBitmap.asImageBitmap(),
                contentScale = ContentScale.Fit,
                contentDescription = null,
                alpha = 1f
            )
        }



        AnimatedToolbarContainer(
            toolbarVisible = toolbarVisible,
            modifier = bottomToolbarModifier(bottomToolbar)
        ) {
            BottomToolBarStatic(
                modifier = Modifier,
                toolbarHeight = bottomToolbarHeight,
                toolbarItems = bottomToolbarItems,
                onEvent = mOnBottomToolbarEvent
            )
        }


    }
}



@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewEditorScreen() {
    QuickEditTheme {
        EditorScreenLayout(
            modifier = Modifier,
            currentBitmap = ImageBitmap.imageResource(id = R.drawable.placeholder_image_3).asAndroidBitmap(),
            undoEnabled = false,
            redoEnabled = false,
            onUndo = {},
            onRedo = {},
            onBottomToolbarEvent = {},
            goToMainScreen = {}
        )
    }
}