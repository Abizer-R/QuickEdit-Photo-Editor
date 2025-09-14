package io.github.abizerr.quickedit.tool.text

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import io.github.abizerr.quickedit.ui.api.QuickEditState
import io.github.abizerr.quickedit.ui.api.ToolContribution
import io.github.abizerr.quickedit.ui.api.ToolController

class TextContribution : ToolContribution {
    override val id: String = "text"
    override val label: String = "Text"

    @Composable
    override fun ToolbarIcon(selected: Boolean, onClick: () -> Unit) {
        IconWithLabel(
            selected = selected,
            imageVector = Icons.Default.TextFields,
            labelText = label,
            onClick = onClick
        )
    }

    @Composable
    override fun Panel(state: QuickEditState, controller: ToolController) {
        Text("Text tool placeholder")
    }
}