package io.github.abizerr.quickedit.ui.api

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import io.github.abizerr.quickedit.engine.api.EditEngine
import io.github.abizerr.quickedit.engine.api.EditImage
import io.github.abizerr.quickedit.engine.api.EditOp
import io.github.abizerr.quickedit.engine.api.EditSnapshot
import io.github.abizerr.quickedit.engine.api.EditedImage
import io.github.abizerr.quickedit.engine.api.SaveFormat
import io.github.abizerr.quickedit.engine.impl.DefaultEditEngine

data class QuickEditConfig(
    val tools: List<ToolContribution> = emptyList(),
    val maxUndo: Int = 20,
    val defaultFormat: SaveFormat = SaveFormat.Jpeg(90)
)

@Composable
fun QuickEditEditor(
    image: EditImage?,
    config: QuickEditConfig = QuickEditConfig(),
    engine: EditEngine = remember { DefaultEditEngine(config.maxUndo) },
    onSave: (Result<EditedImage>) -> Unit = {}
) {

    var snapshot by remember(image) {
        // Initialized in the "LaunchedEffect" block below
        // Reason: engine.newSession(image) is a suspend function
        mutableStateOf<EditSnapshot?>(null)
    }
    LaunchedEffect(image) {
        image?.let { engine.newSession(it) }
    }

    val state = snapshot?.let { QuickEditState(it) }

    // Controller wires to the engine's apply/undo/redo
    val controller = remember(engine, snapshot) {
        object : ToolController {
            override fun emit(op: EditOp) {
                val s = snapshot ?: return
                // TODO: Below function is a suspend function, need coroutineScope to use
//                snapshot = engine.apply(op)
            }

            override fun undo() {
                // TODO: Below function is a suspend function, need coroutineScope to use
//                snapshot = engine.apply(EditOp.Undo)
            }

            override fun redo() {
                // TODO: Below function is a suspend function, need coroutineScope to use
//                snapshot = engine.apply(EditOp.Redo)
            }

        }
    }



    Box(Modifier.fillMaxSize()) {
        Text("QuickEdit placeholder UI")
    }

    // TODO (revamp): We can add a simple Save button later that calls: onSave(engine.save(snapshot!!, config.defaultFormat))
}
