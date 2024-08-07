package com.abizer_r.quickedit.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.abizer_r.quickedit.theme.QuickEditTheme

@Composable
fun QuickEditApp() {
    QuickEditTheme {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
        ) {
            QuickEditNavigation()
        }
    }
}