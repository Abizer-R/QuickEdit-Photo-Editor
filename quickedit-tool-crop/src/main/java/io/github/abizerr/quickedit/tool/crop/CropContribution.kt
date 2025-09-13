package io.github.abizerr.quickedit.tool.crop

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Crop
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import io.github.abizerr.quickedit.ui.api.QuickEditState
import io.github.abizerr.quickedit.ui.api.ToolContribution
import io.github.abizerr.quickedit.ui.api.ToolController

class CropContribution : ToolContribution {
  override val id: String = "crop"

  @Composable
  override fun ToolbarIcon(selected: Boolean, onClick: () -> Unit) {
    // Placeholder icon
    Icon(imageVector = Icons.Outlined.Crop, contentDescription = "Crop")
  }

  @Composable
  override fun Panel(state: QuickEditState, controller: ToolController) {
    Text("Crop tool placeholder")
  }
}
