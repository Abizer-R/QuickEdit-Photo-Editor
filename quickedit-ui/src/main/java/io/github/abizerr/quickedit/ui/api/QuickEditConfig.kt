package io.github.abizerr.quickedit.ui.api

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

data class QuickEditConfig(
    val tools: List<ToolContribution> = emptyList(),
    val maxUndo: Int = 20
)

@Composable
fun QuickEditEditor(
    image: Any? = null, // placeholder type, we’ll replace later
    config: QuickEditConfig = QuickEditConfig(),
    state: QuickEditState = QuickEditState(),
    onSave: (Result<Any>) -> Unit = {}
) {
    Box(Modifier.fillMaxSize()) {
        Text("QuickEdit placeholder UI")
    }
}
