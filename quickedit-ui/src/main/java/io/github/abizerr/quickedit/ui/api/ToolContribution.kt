package io.github.abizerr.quickedit.ui.api

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
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

    @Composable
    fun Modifier.selectionModifier(selected: Boolean): Modifier {
        return if (selected.not()) {
            this
                .padding(horizontal = 8.dp, vertical = 4.dp)
        } else {
            this
                .clip(RoundedCornerShape(3.dp))
                .background(MaterialTheme.colorScheme.onBackground)
                .padding((0.5).dp)
                .clip(RoundedCornerShape(3.dp))
                .background(Color.DarkGray)
                .padding(horizontal = 8.dp, vertical = 4.dp)
        }
    }

    @Composable
    fun IconWithLabel(
        selected: Boolean,
        imageVector: ImageVector,
        labelText: String,
        onClick: () -> Unit
    ) {
        val imageSize = 28.dp
        val labelTextStyle = MaterialTheme.typography.bodySmall

        Column(
            modifier = Modifier
                .clickable { onClick() }
                .selectionModifier(selected),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                modifier = Modifier.size(imageSize),
                contentDescription = labelText,
                imageVector = imageVector,
                colorFilter = ColorFilter.tint(
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
            Spacer(modifier = Modifier.size(4.dp))
            Text(
                style = labelTextStyle,
                text = labelText
            )
        }
    }
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
