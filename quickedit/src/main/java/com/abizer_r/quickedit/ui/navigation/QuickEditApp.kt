package com.abizer_r.quickedit.ui.navigation

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.abizer_r.quickedit.theme.QuickEditTheme
import io.github.abizerr.quickedit.ui.api.QuickEditConfig
import io.github.abizerr.quickedit.ui.api.QuickEditEditor
import io.github.abizerr.quickedit.ui.api.ToolContribution

/**
 * Legacy entry kept for binary/source compatibility.
 * Internally delegates to the new :quickedit-compose-ui editor.
 *
 */
@Composable
fun QuickEditApp(
    initialImageUri: Uri? = null
) {
    QuickEditTheme {
        Scaffold(
            // Ensure content avoids system bars & cutouts
            contentWindowInsets = WindowInsets.safeDrawing,
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                // Old navigation (removed intentionally)
//                QuickEditNavigation(initialImageUri)


                // Temporary: pass through the Uri as a lightweight handle.
                // :quickedit-ui currently accepts 'image: Any?'.
                // In future, this becomes a real EditImage (engine.api) + proper mapping.
                val placeholderImage: Any? = initialImageUri

                QuickEditEditor(
                    image = placeholderImage,
                    config = QuickEditConfig(
                        tools = emptyList<ToolContribution>(), // TODO (revamp): We'll inject real tools in future
                        maxUndo = 20
                    ),
//                    state = QuickEditState(),
                    onSave = {
                        // TODO (revamp): map Result<EditedImage> back if needed
                    }
                )
            }
        }
    }
}