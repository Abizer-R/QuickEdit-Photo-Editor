package io.github.abizerr.quickedit.ui.api

import androidx.compose.runtime.Composable

interface ToolContribution {
  val id: String
  @Composable fun ToolbarIcon(selected: Boolean, onClick: () -> Unit)
  @Composable fun Panel(state: QuickEditState, controller: ToolController)
}

interface ToolController {
  fun emit(op: Any) // placeholder; will be EditOp in future
  fun undo()
  fun redo()
}

class QuickEditState internal constructor()
