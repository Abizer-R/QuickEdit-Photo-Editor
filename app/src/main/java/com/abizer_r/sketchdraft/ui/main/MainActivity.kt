package com.abizer_r.sketchdraft.ui.main

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.abizer_r.components.theme.SketchDraftTheme
import com.abizer_r.touchdraw.R
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
    SketchDraftTheme {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = "editorScreen"
        ) {
            composable(route = "editorScreen") {
                EditorScreen(
                    onTextToolClicked = {
                        navController.navigate("textMode")
                    }
                )
            }
            composable(route = "textMode") { entry ->
                TextModeScreen(
                    imageBitmap = ImageBitmap.imageResource(id = R.drawable.placeholder_image_1),
                    onBackPressed = {
                        navController.popBackStack()
                    },
                    onDoneClicked = {

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