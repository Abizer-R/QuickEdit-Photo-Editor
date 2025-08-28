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
                QuickEditNavigation(initialImageUri)
            }
        }
    }
}