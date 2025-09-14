package io.github.abizerr.quickedit.ui.api

import android.content.ContentResolver
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntSize
import io.github.abizerr.quickedit.engine.api.EditEngine
import io.github.abizerr.quickedit.engine.api.EditImage
import io.github.abizerr.quickedit.engine.api.EditOp
import io.github.abizerr.quickedit.engine.api.EditSnapshot
import io.github.abizerr.quickedit.engine.api.EditedImage
import io.github.abizerr.quickedit.engine.api.SaveFormat
import io.github.abizerr.quickedit.engine.api.Size
import io.github.abizerr.quickedit.engine.impl.DefaultEditEngine
import kotlinx.coroutines.launch
import kotlin.math.max

data class QuickEditConfig(
    val tools: List<ToolContribution> = emptyList(),
    val maxUndo: Int = 20,
    val defaultFormat: SaveFormat = SaveFormat.Jpeg(90)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickEditEditor(
    image: EditImage?,
    config: QuickEditConfig = QuickEditConfig(),
    engine: EditEngine = rememberEngine(config.maxUndo),
    onSave: (Result<EditedImage>) -> Unit = {}
) {

    var snapshot by remember { mutableStateOf<EditSnapshot?>(null) }
    var viewport by remember { mutableStateOf(IntSize.Zero) }
    var preview by remember { mutableStateOf<Bitmap?>(null) }
    val scope = rememberCoroutineScope()

    // Start/replace session when image changes
    LaunchedEffect(image, engine) {
        snapshot = image?.let { engine.newSession(it) }
    }

    // Render when snapshot or viewport changes
    LaunchedEffect(snapshot, viewport) {
        if (viewport.width <= 0 || viewport.height <= 0) return@LaunchedEffect
        val renderResult = engine.render(
            snapshot = snapshot ?: return@LaunchedEffect,
            size = Size(viewport.width, viewport.height)
        )
        preview = renderResult.preview
    }

    val state = snapshot?.let { QuickEditState(it) }

    // Controller wires to the engine's apply/undo/redo
    val controller = remember(engine, snapshot) {
        object : ToolController {
            override fun emit(op: EditOp) {
                scope.launch { snapshot = engine.apply(op) }
            }

            override fun undo() {
                scope.launch { snapshot = engine.apply(EditOp.Undo) }
            }

            override fun redo() {
                scope.launch { snapshot = engine.apply(EditOp.Redo) }
            }

        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("QuickEdit") },
                actions = {
                    TextButton(
                        enabled = snapshot != null,
                        onClick = {
                            val mSnapshot = snapshot ?: return@TextButton
                            scope.launch {
                                onSave(engine.save(mSnapshot, config.defaultFormat))
                            }
                        }
                    ) {
                        Text("Save")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding)
                .onSizeChanged { viewport = it }
        ) {
            if (preview != null) {
                Image(
                    bitmap = preview!!.asImageBitmap(),
                    contentDescription = "Preview"
                )
            } else {
                Text("Loading...")
            }

            // FUTURE: Toolbars/panels using config.tools
        }

    }
}

@Composable
private fun rememberEngine(maxUndo: Int): EditEngine {
    val resolver: ContentResolver = LocalContext.current.contentResolver
    return remember(resolver, maxUndo) {
        DefaultEditEngine(
            maxUndo = maxUndo,
            resolver = resolver
        )
    }
}