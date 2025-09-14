package io.github.abizerr.quickedit.tool.draw

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Brush
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import io.github.abizerr.quickedit.ui.api.QuickEditState
import io.github.abizerr.quickedit.ui.api.ToolContribution
import io.github.abizerr.quickedit.ui.api.ToolController

class DrawContribution : ToolContribution {
    override val id: String = "draw"
    override val label: String = "Draw"

    @Composable
    override fun ToolbarIcon(selected: Boolean, onClick: () -> Unit) {
        IconWithLabel(
            selected = selected,
            imageVector = Icons.Outlined.Brush,
            labelText = label,
            onClick = onClick
        )
    }

    @Composable
    override fun Panel(state: QuickEditState, controller: ToolController) {
        Text("Draw tool placeholder")
    }
}