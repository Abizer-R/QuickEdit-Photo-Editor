package com.abizer_r.sketchdraft.ui.main

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.abizer_r.components.theme.SketchDraftTheme
import com.abizer_r.touchdraw.R
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
    
    SketchDraftTheme {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = "editorScreen"
        ) {
            composable(route = "editorScreen") {



//                EditorScreen()
                DrawModeScreen(
                    bitmap = mainActivityViewModel.bitmap,
                    goToTextModeScreen = {
                        mainActivityViewModel.bitmap = it.copy(Bitmap.Config.ARGB_8888, false)
                        navController.navigate("textMode")
                    }
                )

            }
            composable(route = "textMode") { entry ->
                TextModeScreen(
                    bitmap = mainActivityViewModel.bitmap?.asImageBitmap()
                        ?: ImageBitmap.imageResource(id = R.drawable.placeholder_image_1),
                    onBackPressed = {
                        navController.popBackStack()
                    },
                    onDoneClicked = {
                        mainActivityViewModel.bitmap = it.copy(Bitmap.Config.ARGB_8888, false)
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