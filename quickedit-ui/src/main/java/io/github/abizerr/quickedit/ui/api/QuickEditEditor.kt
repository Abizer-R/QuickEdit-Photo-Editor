package io.github.abizerr.quickedit.ui.api

import android.content.ContentResolver
import android.graphics.Bitmap
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import io.github.abizerr.quickedit.engine.api.EditEngine
import io.github.abizerr.quickedit.engine.api.EditImage
import io.github.abizerr.quickedit.engine.api.EditOp
import io.github.abizerr.quickedit.engine.api.EditSnapshot
import io.github.abizerr.quickedit.engine.api.EditedImage
import io.github.abizerr.quickedit.engine.api.SaveFormat
import io.github.abizerr.quickedit.engine.api.Size
import io.github.abizerr.quickedit.engine.impl.DefaultEditEngine
import kotlinx.coroutines.launch

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

    var selectedToolId: String? by remember(config.tools) {
        mutableStateOf(config.tools.firstOrNull()?.id)
    }
    val selectedTool: ToolContribution? = remember(selectedToolId, config.tools) {
        config.tools.firstOrNull { it.id == selectedToolId }

    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("QuickEdit") },
                actions = {
                    TextButton(
                        enabled = engine.history.canUndo,
                        onClick = { controller.undo() }
                    ) { Text("Undo") }

                    TextButton(
                        enabled = engine.history.canRedo,
                        onClick = { controller.redo() }
                    ) { Text("Redo") }

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
        },
        bottomBar = {
            if (config.tools.isNotEmpty()) {
                BottomAppBar {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        config.tools.forEach { tool ->
                            // Delegate icon rendering to the tool
                            tool.ToolbarIcon(
                                selected = tool.id == selectedToolId,
                                onClick = { selectedToolId = tool.id }
                            )
                        }
                    }
                }
            }


        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .onSizeChanged { viewport = it },
                contentAlignment = Alignment.Center
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

            AnimatedContent(
                targetState = selectedTool?.id,
                label = "tool-panel",
                transitionSpec = {
                    fadeIn(tween(150))
                        .togetherWith(fadeOut(tween(150)))
                }
            ) { toolId ->
                val tool = config.tools.firstOrNull { it.id == toolId }
                if (tool != null && state != null) {
                    // Panel renders below preview (like your old bottom sheet/panel)
                    Surface(
                        tonalElevation = 2.dp
                    ) {
                        tool.Panel(state, controller)
                    }
                } else {
                    Spacer(Modifier.height(0.dp))
                }

            }

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