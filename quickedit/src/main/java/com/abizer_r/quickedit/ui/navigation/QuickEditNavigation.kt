package com.abizer_r.quickedit.ui.navigation

import android.graphics.Bitmap
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.IntOffset
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.abizer_r.quickedit.ui.SharedEditorViewModel
import com.abizer_r.quickedit.ui.cropMode.CropperScreen
import com.abizer_r.quickedit.ui.drawMode.DrawModeScreen
import com.abizer_r.quickedit.ui.editorScreen.EditorScreen
import com.abizer_r.quickedit.ui.editorScreen.EditorScreenState
import com.abizer_r.quickedit.ui.effectsMode.EffectsModeScreen
import com.abizer_r.quickedit.ui.mainScreen.MainScreen
import com.abizer_r.quickedit.ui.textMode.TextModeScreen
import com.abizer_r.quickedit.utils.other.anim.AnimUtils
import com.abizer_r.quickedit.utils.other.anim.getDefaultEnterTransition
import com.abizer_r.quickedit.utils.other.anim.getDefaultExitTransition
import com.abizer_r.quickedit.utils.other.bitmap.ImmutableBitmap
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun QuickEditNavigation() {
    val lifeCycleOwner = LocalLifecycleOwner.current
    val lifecycleScope = lifeCycleOwner.lifecycleScope

    val sharedEditorViewModel = hiltViewModel<SharedEditorViewModel>()
    val navController = rememberNavController()

    val onImageSelected = remember<(Bitmap) -> Unit> {{ bitmap ->
        sharedEditorViewModel.addBitmapToStack(
            bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, false),
            triggerRecomposition = false
        )
        sharedEditorViewModel.useTransition = true
        navController.navigate(NavDestinations.EDITOR_SCREEN)
    }}

    val goToCropModeScreenLambda = remember<(EditorScreenState) -> Unit> {{ finalEditorState ->
        sharedEditorViewModel.updateStacksFromEditorState(finalEditorState)
        navController.navigate(NavDestinations.CROPPER_SCREEN)
    }}
    val goToDrawModeScreenLambda = remember<(EditorScreenState) -> Unit> {{ finalEditorState ->
        sharedEditorViewModel.updateStacksFromEditorState(finalEditorState)
        navController.navigate(NavDestinations.DRAW_MODE_SCREEN)
    }}
    val goToTextModeScreenLambda = remember<(EditorScreenState) -> Unit> {{ finalEditorState ->
        sharedEditorViewModel.updateStacksFromEditorState(finalEditorState)
        navController.navigate(NavDestinations.TEXT_MODE_SCREEN)
    }}
    val goToEffectsModeScreenLambda = remember<(EditorScreenState) -> Unit> {{ finalEditorState ->
        sharedEditorViewModel.updateStacksFromEditorState(finalEditorState)
        navController.navigate(NavDestinations.EFFECTS_MODE_SCREEN)
    }}
    val goToMainScreenLambda = remember<() -> Unit> {{
        sharedEditorViewModel.resetStacks()
        sharedEditorViewModel.useTransition = true
        navController.navigate(NavDestinations.MAIN_SCREEN)
    }}


    val onBackPressedLambda = remember<() -> Unit> {{
        navController.navigateUp()
    }}
    val onDoneClickedLambda = remember<(Bitmap) -> Unit> {{
        sharedEditorViewModel.addBitmapToStack(
            bitmap = it.copy(Bitmap.Config.ARGB_8888, false),
        )
        navController.navigate(
            NavDestinations.EDITOR_SCREEN,
            navOptions = NavOptions.Builder()
                .setPopUpTo(route = NavDestinations.EDITOR_SCREEN, inclusive = true)
                .build()
        )
    }}


    NavHost(
        navController = navController,
        startDestination = NavDestinations.MAIN_SCREEN,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {

        /**
         * TODO - Make the transition between main screen and editor screen smooth and nice
         */
        composable(
            route = NavDestinations.MAIN_SCREEN,
            enterTransition = { getDefaultEnterTransition() },
            popEnterTransition = { getDefaultEnterTransition() },
            exitTransition = { getDefaultExitTransition() },
            popExitTransition = { getDefaultExitTransition() }
        ) {
            MainScreen(onImageSelected = onImageSelected)
        }

        composable(
            route = NavDestinations.EDITOR_SCREEN,
            enterTransition = {
                if (sharedEditorViewModel.useTransition) getDefaultEnterTransition()
                else EnterTransition.None
            },
            popEnterTransition = {
                if (sharedEditorViewModel.useTransition) getDefaultEnterTransition()
                else EnterTransition.None
            },
            exitTransition = {
                if (sharedEditorViewModel.useTransition) getDefaultExitTransition()
                else ExitTransition.None
            },
            popExitTransition = {
                if (sharedEditorViewModel.useTransition) getDefaultExitTransition()
                else ExitTransition.None
            }
        ) {
            sharedEditorViewModel.useTransition = false
            /**
             * TODO - FIX BUG: undo and redo not resetting when selecting another image by going back to main screen
             * -> select image -> do some stuff -> go back -> select another image -> UNDO/REDO DOESN'T RESET!!!!
             */
            val initialEditorState = EditorScreenState(
                sharedEditorViewModel.bitmapStack, sharedEditorViewModel.bitmapRedoStack
            )

            EditorScreen(
                initialEditorScreenState = initialEditorState,
                goToCropModeScreen = goToCropModeScreenLambda,
                goToDrawModeScreen = goToDrawModeScreenLambda,
                goToTextModeScreen = goToTextModeScreenLambda,
                goToEffectsModeScreen = goToEffectsModeScreenLambda,
                goToMainScreen = goToMainScreenLambda
            )

        }

        composable(route = NavDestinations.CROPPER_SCREEN) { entry ->
            CropperScreen(
                immutableBitmap = ImmutableBitmap((sharedEditorViewModel.getCurrentBitmap())),
                onBackPressed = onBackPressedLambda,
                onDoneClicked = onDoneClickedLambda,
            )
        }

        composable(route = NavDestinations.DRAW_MODE_SCREEN) { entry ->
            DrawModeScreen(
                immutableBitmap = ImmutableBitmap((sharedEditorViewModel.getCurrentBitmap())),
                onBackPressed = onBackPressedLambda,
                onDoneClicked = onDoneClickedLambda,
            )
        }

        composable(route = NavDestinations.TEXT_MODE_SCREEN) { entry ->
            TextModeScreen(
                immutableBitmap = ImmutableBitmap(sharedEditorViewModel.getCurrentBitmap()),
                onBackPressed = onBackPressedLambda,
                onDoneClicked = onDoneClickedLambda,
            )
        }

        composable(route = NavDestinations.EFFECTS_MODE_SCREEN) { entry ->
            EffectsModeScreen(
                immutableBitmap = ImmutableBitmap(sharedEditorViewModel.getCurrentBitmap()),
                onBackPressed = onBackPressedLambda,
                onDoneClicked = onDoneClickedLambda,
            )
        }
    }
}