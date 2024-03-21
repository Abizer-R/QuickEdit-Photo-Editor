package com.abizer_r.sketchdraft

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.abizer_r.sketchdraft.ui.drawingCanvas.DrawingCanvas
import com.abizer_r.sketchdraft.ui.drawingCanvas.PathDetails
import com.abizer_r.sketchdraft.ui.drawingCanvas.controllerBottomSheet.BrushControllerBottomSheet
import com.abizer_r.sketchdraft.ui.theme.SketchDraftTheme
import com.abizer_r.sketchdraft.util.AppUtils
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SketchDraftTheme {

                val colorList = AppUtils.colorList
                var opacity by remember { mutableStateOf(100) }
                var strokeWidth by remember { mutableStateOf(6) }
                var selectedColorIndex by remember { mutableStateOf(0) }
                val pathList = remember { mutableListOf<PathDetails>() }

                val scaffoldState = rememberBottomSheetScaffoldState()
                val dragHandleIconSize = 32
                BottomSheetScaffold(
                    scaffoldState = scaffoldState,
                    sheetDragHandle = {
                        Image(
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .size(dragHandleIconSize.dp),
                            imageVector = Icons.Outlined.KeyboardArrowUp,
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(color = Color.White)
                        )
                    },
                    sheetContent = {
                        BrushControllerBottomSheet(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            initialValueOpacity = opacity,
                            onOpacityChanged = {
                                opacity = it.roundToInt()
                            },
                            initialValueStrokeWidth = strokeWidth,
                            onStrokeWidthChanged = {
                                strokeWidth = it.roundToInt()
                            },
                            selectedColorIndex = selectedColorIndex,
                            colorList = AppUtils.colorList,
                            onColorIdxChanged = { idx ->
                                selectedColorIndex = idx
                            }
                        )
                    }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it)
                            .background(MaterialTheme.colorScheme.background)
                    ) {
                        DrawingCanvas(
                            modifier = Modifier.fillMaxSize(),
                            strokeWidth = strokeWidth.toFloat(),
                            strokeOpacity = opacity.toFloat(),
                            strokeColor = colorList[selectedColorIndex],
                            pathList = pathList,
                            addPathToList = { pathDetails ->
                                pathList.add(pathDetails)
                            }
                        )
                    }
                }
            }
        }
    }
}