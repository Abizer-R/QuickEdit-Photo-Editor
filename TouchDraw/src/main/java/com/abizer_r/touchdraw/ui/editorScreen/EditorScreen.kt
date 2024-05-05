package com.abizer_r.touchdraw.ui.editorScreen

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.abizer_r.components.theme.SketchDraftTheme
import com.abizer_r.touchdraw.ui.drawingCanvas.DrawingCanvas
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.BottomToolBar
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.BottomToolbarItems
import com.abizer_r.touchdraw.ui.editorScreen.topToolbar.TopToolBar

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EditorScreen() {

    var selectedToolType: BottomToolbarItems by remember {
        mutableStateOf(BottomToolbarItems.Brush)
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val (topToolbar, bottomToolbar, drawingCanvas) = createRefs()

        TopToolBar(
            modifier = Modifier.constrainAs(topToolbar) {
                top.linkTo(parent.top)
                width = Dimension.matchParent
                height = Dimension.wrapContent
            },
            onUndo = {},
            onRedo = {}
        )

        DrawingCanvas(
            modifier = Modifier.constrainAs(drawingCanvas) {
                top.linkTo(topToolbar.bottom)
                bottom.linkTo(bottomToolbar.top)
                width = Dimension.matchParent
                height = Dimension.fillToConstraints
            }
        )


        BottomToolBar(
            modifier = Modifier.constrainAs(bottomToolbar) {
                bottom.linkTo(parent.bottom)
                width = Dimension.matchParent
                height = Dimension.wrapContent
            },
            selectedColor = Color.Yellow,
            selectedToolbarType = selectedToolType,
            onToolItemClicked = {
                when (it) {
                    BottomToolbarItems.Color -> {
                        // TODO: open color picker dialog
                    }
                    else -> {
                        selectedToolType = it
                    }
                }
            }
        )


//        ColorPickerDialog(
//            show = controllerBsState.showColorDialog,
//            type = ColorPickerType.Classic(true),
//            properties = DialogProperties(),
//            onDismissRequest = {},
//            onPickedColor = {
//                onEvent(
//                    ControllerBSEvents.ColorSelected(
//                        -1, color = it
//                    )
//                )
//            }
//        )

    }
}



@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewEditorScreen() {
    SketchDraftTheme {
        EditorScreen()
    }
}