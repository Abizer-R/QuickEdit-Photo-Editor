package io.github.abizerr.quickedit.ui.api

import androidx.compose.runtime.Composable
import io.github.abizerr.quickedit.engine.api.EditOp
import io.github.abizerr.quickedit.engine.api.EditSnapshot

interface ToolContribution {
    val id: String
    val label: String get() = id
    @Composable
    fun ToolbarIcon(selected: Boolean, onClick: () -> Unit)
    @Composable
    fun Panel(state: QuickEditState, controller: ToolController)
    /**
     * FUTURE: Add optional lifecycle hooks without breaking:
     * fun onAttach() {}
     * fun onDetach() {}
     * val requiredOps: Set<KClass<out EditOp>> get() = emptySet()
     */
}

interface ToolController {
    fun emit(op: EditOp) // placeholder; will be EditOp in future
    fun undo()
    fun redo()
    /**
     * FUTURE: add batch ops for atomic changes
     * fun emitAll(ops: List<EditOp>)
     */
}

class QuickEditState internal constructor(
    val snapshot: EditSnapshot
)
