package io.github.abizerr.quickedit.ui.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import io.github.abizerr.quickedit.engine.api.EditImage
import io.github.abizerr.quickedit.engine.api.EditOp
import io.github.abizerr.quickedit.engine.api.EditSnapshot
import io.github.abizerr.quickedit.ui.api.QuickEditState
import io.github.abizerr.quickedit.ui.api.ToolContribution
import io.github.abizerr.quickedit.ui.api.ToolController

object PreviewUtils {

    @Composable
    fun getDummyEditorState(): QuickEditState {
        val snapShot = EditSnapshot(image = EditImage.FromBitmap(getDummyBitmap()))
        return QuickEditState(snapShot)
    }

    fun getDummyTools(): List<ToolContribution> {
        val tools = arrayListOf<ToolContribution>()
        repeat(4) { i ->
            tools.add(getDummyToolContribution(i))
        }
        return tools
    }

    private fun getDummyToolContribution(i: Int = 0): ToolContribution {
        val dummyToolContribution = object : ToolContribution {
            override val id: String get() = "dummy$i"
            @Composable
            override fun Panel(state: QuickEditState, controller: ToolController) { }
            @Composable
            override fun ToolbarIcon(selected: Boolean, onClick: () -> Unit) {
                IconWithLabel(
                    selected = selected,
                    imageVector = Icons.Default.Edit,
                    labelText = "Tool-$i",
                    onClick = {}
                )
            }
        }
        return dummyToolContribution
    }

    fun getDummyToolController(): ToolController {
        val dummyController = object : ToolController {
            override fun emit(op: EditOp) {}
            override fun undo() {}
            override fun redo() {}
        }
        return dummyController
    }
}