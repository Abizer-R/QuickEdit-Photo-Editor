package com.abizer_r.sketchdraft.ui.main

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.abizer_r.components.theme.SketchDraftTheme
import com.abizer_r.touchdraw.ui.drawMode.DrawModeScreen
import com.abizer_r.touchdraw.ui.editorScreen.EditorScreen
import com.abizer_r.touchdraw.ui.textMode.TextModeScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen() {
    /**
     * TODO: Watch the below video
     * TODO: Sharing data between compose navigation screens = https://www.youtube.com/watch?v=h61Wqy3qcKg&ab_channel=PhilippLackner
     */
    val mainActivityViewModel: MainActivityViewModel = viewModel()
    val initialBitmap = ImageBitmap.imageResource(
        id = com.abizer_r.components.R.drawable.placeholder_image_2
    ).asAndroidBitmap()
    mainActivityViewModel.addBitmapToStack(initialBitmap)

    SketchDraftTheme {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = "editorScreen"
        ) {

            composable(route = "editorScreen") {

                val lifeCycleOwner = LocalLifecycleOwner.current
                val editorScreenState by mainActivityViewModel.editorScreenState.collectAsStateWithLifecycle(
                    lifecycleOwner = lifeCycleOwner
                )
                EditorScreen(
                    bitmap = mainActivityViewModel.currentBitmap,
                    editorScreenState = editorScreenState,
                    enableUndo = mainActivityViewModel.undoEnabled,
                    enableRedo = mainActivityViewModel.redoEnabled,
                    onUndo = mainActivityViewModel::onUndo,
                    onRedo = mainActivityViewModel::onRedo,
                    goToDrawModeScreen = {
                        navController.navigate("drawMode")
                    },
                    goToTextModeScreen = {
                        navController.navigate("textMode")
                    },
                )

            }
            composable(route = "drawMode") {
                DrawModeScreen(
                    bitmap = mainActivityViewModel.currentBitmap,
                    onBackPressed = {
                        navController.popBackStack()
                    },
                    onDoneClicked = {
                        mainActivityViewModel.addBitmapToStack(it.copy(Bitmap.Config.ARGB_8888, false))
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
                    bitmap = mainActivityViewModel.currentBitmap.asImageBitmap(),
                    onBackPressed = {
                        navController.popBackStack()
                    },
                    onDoneClicked = {
                        mainActivityViewModel.addBitmapToStack(it.copy(Bitmap.Config.ARGB_8888, false))
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
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun MainScreenPreview() {

    SketchDraftTheme {
        MainScreen()
    }
}