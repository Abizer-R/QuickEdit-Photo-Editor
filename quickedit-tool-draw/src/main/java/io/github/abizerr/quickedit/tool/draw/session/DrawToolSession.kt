package io.github.abizerr.quickedit.tool.draw.session

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.abizerr.quickedit.ui.api.ToolController
import io.github.abizerr.quickedit.ui.api.ToolHost
import io.github.abizerr.quickedit.ui.api.ToolSession

internal class DrawToolSession : ToolSession {
    override val id: String get() = "draw"

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Ui(host: ToolHost, controller: ToolController, onExit: () -> Unit) {
        // Ask shell for exclusive gestures while this full-screen tool is active

        // Placeholder
        Surface(modifier = Modifier.Companion.fillMaxSize()) {
            Column(Modifier.Companion.fillMaxSize()) {
                TopAppBar(
                    title = { Text("Draw (stub)") },
                    actions = {
                        TextButton(onClick = onExit) { Text("Done") }
                    }
                )
                Box(
                    modifier = Modifier.Companion
                        .fillMaxSize()
                        .padding(16.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Companion.Center
                ) {
                    Text("Canvas placeholder — wiring test")
                }
            }
        }
    }
}