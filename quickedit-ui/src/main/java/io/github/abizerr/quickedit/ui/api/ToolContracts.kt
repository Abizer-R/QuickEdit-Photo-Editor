package io.github.abizerr.quickedit.ui.api

import androidx.compose.runtime.Composable
import io.github.abizerr.quickedit.engine.api.EditOp
import io.github.abizerr.quickedit.engine.api.EditSnapshot

interface ToolHost {
    /** Latest immutable snapshot produced by the engine. */
    val snapshot: EditSnapshot

    /** Hint the shell to switch to gesture policy (e.g., disable editor pan/zoom */
    fun requestGestureMode(mode: GestureMode)
}

enum class GestureMode { EditorGestures, ToolExclusive }

/** A running instance of a tool (state + Composabel UI) */
interface ToolSession {
    /** Stable identifier (e.g., "draw", "crop") */
    val id: String

    /** Tool's full-screen UI; shell passes in host, controller & exit callback */
    @Composable
    fun Ui(host: ToolHost, controller: ToolController, onExit: () -> Unit)

    /** Optional: flush pending ops on exit. Kept simple for now. */
    fun onClose(): List<EditOp> = emptyList()
}

/** Factory keeps the module boundary DI-free. */
fun interface ToolFactory {
    fun create(params: ToolParams): ToolSession
}

/** Arbitrary inputs supplied by host (e.g., initial brush/ratio) */
data class ToolParams(
    val initial: Map<String, Any?> = emptyMap()
)