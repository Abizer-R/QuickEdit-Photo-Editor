package com.abizer_r.quickedit.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import com.abizer_r.quickedit.ui.navigation.QuickEditApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        val imageUri: Uri? = extractImageUriFromIntent(intent)

        setContent {
            QuickEditApp(initialImageUri = imageUri)
        }
    }

    private fun extractImageUriFromIntent(intent: Intent?): Uri? {
        if (intent == null) return null

        return if (intent.action == Intent.ACTION_SEND) {
            intent.getParcelableExtra(Intent.EXTRA_STREAM)
        } else {
            null
        }
    }
}