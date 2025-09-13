package io.github.abizerr.quickedit.tool.effects

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import io.github.abizerr.quickedit.ui.api.QuickEditState
import io.github.abizerr.quickedit.ui.api.ToolContribution
import io.github.abizerr.quickedit.ui.api.ToolController

class EffectsContribution : ToolContribution {
    override val id: String = "effects"

    @Composable
    override fun ToolbarIcon(selected: Boolean, onClick: () -> Unit) {
        // Placeholder icon
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_effects),
            contentDescription = "Effects"
        )
    }

    @Composable
    override fun Panel(state: QuickEditState, controller: ToolController) {
        Text("Effects tool placeholder")
    }
}