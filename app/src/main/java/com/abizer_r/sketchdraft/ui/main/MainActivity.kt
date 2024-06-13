package com.abizer_r.sketchdraft.ui.main

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.abizer_r.components.theme.SketchDraftTheme
import com.abizer_r.touchdraw.ui.SharedEditorViewModel
import com.abizer_r.touchdraw.ui.drawMode.DrawModeScreen
import com.abizer_r.touchdraw.ui.editorScreen.EditorScreen
import com.abizer_r.touchdraw.ui.editorScreen.EditorScreenState
import com.abizer_r.touchdraw.ui.textMode.TextModeScreen
import dagger.hilt.android.AndroidEntryPoint
import java.util.Stack

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SketchDraftTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    /**
     * This should be passed dynamically
     */
    val initialBitmap = ImageBitmap.imageResource(
        id = com.abizer_r.components.R.drawable.placeholder_image_2
    ).asAndroidBitmap()

    val sharedEditorViewModel: SharedEditorViewModel = hiltViewModel()
    LaunchedEffect(key1 = Unit) {
        sharedEditorViewModel.addBitmapToStack(initialBitmap)
    }

    val lifeCycleOwner = LocalLifecycleOwner.current
    val recompositionTrigger by sharedEditorViewModel.recompositionTrigger.collectAsStateWithLifecycle(
        lifecycleOwner = lifeCycleOwner
    )


    Box(
        modifier = Modifier
            .fillMaxSize()
            // without this bg, user will see a white background for a second
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (recompositionTrigger > 0 && sharedEditorViewModel.bitmapStack.isNotEmpty()) {
            // Adding this check because the default state in viewModel will have empty stack
            // After adding the initialBitmap, we will have non-empty stack

            MainScreenNavigation(
                sharedEditorViewModel = sharedEditorViewModel
            )
        }

    }


}

@Composable
fun MainScreenNavigation(
    sharedEditorViewModel: SharedEditorViewModel,
//    bitmapStack: Stack<Bitmap>,
//    bitmapRedoStack: Stack<Bitmap>,
//    updateStacksFromEditorState
) {
    if (sharedEditorViewModel.bitmapStack.isEmpty()) {
        throw Exception("EmptyStackException: The bitmapStack should contain at least one bitmap")
    }
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "editorScreen"
    ) {

        composable(route = "editorScreen") {
            val initialEditorState = EditorScreenState(
                sharedEditorViewModel.bitmapStack, sharedEditorViewModel.bitmapRedoStack
            )
            EditorScreen(
                initialEditorScreenState = initialEditorState,
                goToDrawModeScreen = { finalEditorState ->
                    sharedEditorViewModel.updateStacksFromEditorState(finalEditorState)
                    navController.navigate("drawMode")
                },
                goToTextModeScreen = { finalEditorState ->
                    sharedEditorViewModel.updateStacksFromEditorState(finalEditorState)
                    navController.navigate("textMode")
                },
            )

        }
        composable(route = "drawMode") { entry ->
            DrawModeScreen(
                bitmap = sharedEditorViewModel.getCurrentBitmap(),
                onBackPressed = {
                    navController.navigateUp()
                },
                onDoneClicked = {
                    Log.d("TEST_RECOMP", "DrawModeScreen: addBitmapToStack, bitmap = $it")
                    sharedEditorViewModel.addBitmapToStack(it.copy(Bitmap.Config.ARGB_8888, false))
                    navController.navigate(
                        "editorScreen",
                        navOptions = NavOptions.Builder()
                            .setPopUpTo(route = "editorScreen", inclusive = true)
                            .build()
                    )
                }
            )
        }
        composable(route = "textMode") { entry ->
            TextModeScreen(
                bitmap = sharedEditorViewModel.getCurrentBitmap().asImageBitmap(),
                onBackPressed = {
                    navController.navigateUp()
                },
                onDoneClicked = {
                    Log.d("TEST_RECOMP", "TextModeScreen: addBitmapToStack, bitmap = $it", )
                    sharedEditorViewModel.addBitmapToStack(it.copy(Bitmap.Config.ARGB_8888, false))
                    navController.navigate(
                        "editorScreen",
                        navOptions = NavOptions.Builder()
                            .setPopUpTo(route = "editorScreen", inclusive = true)
                            .build()
                    )
                }
            )
        }
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun MainScreenPreview() {

    SketchDraftTheme {
        MainScreen()
    }
}