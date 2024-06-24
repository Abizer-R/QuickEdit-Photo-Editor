package com.abizer_r.sketchdraft.ui.main

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.abizer_r.components.theme.SketchDraftTheme
import com.abizer_r.touchdraw.ui.SharedEditorViewModel
import com.abizer_r.touchdraw.ui.common.ErrorView
import com.abizer_r.touchdraw.ui.common.LoadingView
import com.abizer_r.touchdraw.ui.drawMode.DrawModeScreen
import com.abizer_r.touchdraw.ui.editorScreen.EditorScreen
import com.abizer_r.touchdraw.ui.editorScreen.EditorScreenState
import com.abizer_r.touchdraw.ui.effectsMode.EffectsModeScreen
import com.abizer_r.touchdraw.ui.textMode.TextModeScreen
import com.abizer_r.touchdraw.utils.other.bitmap.BitmapUtils
import com.abizer_r.touchdraw.utils.other.bitmap.BitmapStatus
import com.abizer_r.touchdraw.utils.other.bitmap.ImmutableBitmap
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SketchDraftTheme {

                val context = LocalContext.current

                var selectedImageUri by remember {
                    mutableStateOf<Uri?>(null)
                }
                var scaledBitmapStatus by remember {
                    mutableStateOf<BitmapStatus>(BitmapStatus.None)
                }
                val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.PickVisualMedia()
                ) { uri ->
                    selectedImageUri = uri
                }

                LaunchedEffect(key1 = Unit) {
                    singlePhotoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }

                LaunchedEffect(key1 = selectedImageUri) {
                    selectedImageUri?.let { imgUri ->
                        lifecycleScope.launch(Dispatchers.IO) {
                            BitmapUtils.getScaledBitmap(context, imgUri).onEach {
                                scaledBitmapStatus = it
                            }.collect()
                        }
                    }
                }


                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                ) {

                    when (val bitmapStatus = scaledBitmapStatus) {
                        BitmapStatus.None -> {}
                        BitmapStatus.Processing -> {
                            LoadingView(modifier = Modifier.fillMaxSize())
                        }
                        is BitmapStatus.Failed -> {
                            val errorText = bitmapStatus.errorMsg ?: bitmapStatus.exception?.message
                                ?: getString(com.abizer_r.components.R.string.something_went_wrong)
                            ErrorView(
                                modifier = Modifier.align(Alignment.Center),
                                errorText = errorText
                            )
                        }
                        is BitmapStatus.Success -> {
                            MainScreen(mInitialBitmap = bitmapStatus.scaledBitmap)
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun MainScreen(
    mInitialBitmap: Bitmap?
) {
    /**
     * This should be passed dynamically
     */
    val initialBitmap = mInitialBitmap ?: ImageBitmap.imageResource(
        id = com.abizer_r.components.R.drawable.placeholder_image_3
    ).asAndroidBitmap()

    val sharedEditorViewModel: SharedEditorViewModel = hiltViewModel()
    LaunchedEffect(key1 = Unit) {
        sharedEditorViewModel.addBitmapToStack(
            initialBitmap,
            triggerRecomposition = true,
        )
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

            val goToDrawModeScreenLambda = remember<(EditorScreenState) -> Unit> {{ finalEditorState ->
                sharedEditorViewModel.updateStacksFromEditorState(finalEditorState)
                navController.navigate("drawMode")
            }}
            val goToTextModeScreenLambda = remember<(EditorScreenState) -> Unit> {{ finalEditorState ->
                sharedEditorViewModel.updateStacksFromEditorState(finalEditorState)
                navController.navigate("textMode")
            }}
            val goToEffectsModeScreenLambda = remember<(EditorScreenState) -> Unit> {{ finalEditorState ->
                sharedEditorViewModel.updateStacksFromEditorState(finalEditorState)
                navController.navigate("effectsMode")
            }}

            EditorScreen(
                initialEditorScreenState = initialEditorState,
                goToDrawModeScreen = goToDrawModeScreenLambda,
                goToTextModeScreen = goToTextModeScreenLambda,
                goToEffectsModeScreen = goToEffectsModeScreenLambda,
            )

        }

        composable(route = "drawMode") { entry ->

            val onBackPressedLambda = remember<() -> Unit> {{
                navController.navigateUp()
            }}

            val onDoneClickedLambda = remember<(Bitmap) -> Unit> {{
                Log.d("TEST_RECOMP", "DrawModeScreen: addBitmapToStack, bitmap = $it")
                sharedEditorViewModel.addBitmapToStack(
                    bitmap = it.copy(Bitmap.Config.ARGB_8888, false),
                )
                navController.navigate(
                    "editorScreen",
                    navOptions = NavOptions.Builder()
                        .setPopUpTo(route = "editorScreen", inclusive = true)
                        .build()
                )
            }}

            DrawModeScreen(
                immutableBitmap = ImmutableBitmap((sharedEditorViewModel.getCurrentBitmap())),
                onBackPressed = onBackPressedLambda,
                onDoneClicked = onDoneClickedLambda,
            )
        }

        composable(route = "textMode") { entry ->

            val onBackPressedLambda = remember<() -> Unit> {{
                navController.navigateUp()
            }}

            val onDoneClickedLambda = remember<(Bitmap) -> Unit> {{
                Log.d("TEST_RECOMP", "TextModeScreen: addBitmapToStack, bitmap = $it", )
                sharedEditorViewModel.addBitmapToStack(
                    bitmap = it.copy(Bitmap.Config.ARGB_8888, false),
                )
                navController.navigate(
                    "editorScreen",
                    navOptions = NavOptions.Builder()
                        .setPopUpTo(route = "editorScreen", inclusive = true)
                        .build()
                )
            }}

            TextModeScreen(
                immutableBitmap = ImmutableBitmap(sharedEditorViewModel.getCurrentBitmap()),
                onBackPressed = onBackPressedLambda,
                onDoneClicked = onDoneClickedLambda,
            )
        }

        composable(route = "effectsMode") { entry ->

            val onBackPressedLambda = remember<() -> Unit> {{
                navController.navigateUp()
            }}

            val onDoneClickedLambda = remember<(Bitmap) -> Unit> {{
                sharedEditorViewModel.addBitmapToStack(
                    bitmap = it.copy(Bitmap.Config.ARGB_8888, false),
                )
                navController.navigate(
                    "editorScreen",
                    navOptions = NavOptions.Builder()
                        .setPopUpTo(route = "editorScreen", inclusive = true)
                        .build()
                )
            }}

            EffectsModeScreen(
                immutableBitmap = ImmutableBitmap(sharedEditorViewModel.getCurrentBitmap()),
                onBackPressed = onBackPressedLambda,
                onDoneClicked = onDoneClickedLambda,
            )
        }
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun MainScreenPreview() {

    SketchDraftTheme {
        MainScreen(null)
    }
}