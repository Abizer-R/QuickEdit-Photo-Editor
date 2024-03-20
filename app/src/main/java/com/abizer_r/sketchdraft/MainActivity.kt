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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.abizer_r.sketchdraft.ui.drawingCanvas.DrawingCanvas
import com.abizer_r.sketchdraft.ui.theme.SketchDraftTheme
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SketchDraftTheme {

                var opacity by remember { mutableStateOf(100) }
                var strokeWidth by remember { mutableStateOf(2) }

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
                            onValueChangeOpacity = {
                                opacity = it.roundToInt()
                            },
                            initialValueStrokeWidth = strokeWidth,
                            onValueChangeStrokeWidth = {
                                strokeWidth = it.roundToInt()
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
                            strokeOpacity = opacity.toFloat()
                        )
                    }
                }
            }
        }
    }
}

@Composable
@ExperimentalMaterial3Api
fun BrushControllerBottomSheet(
    modifier: Modifier = Modifier,
    initialValueOpacity: Int,
    onValueChangeOpacity: (Float) -> Unit,
    initialValueStrokeWidth: Int,
    onValueChangeStrokeWidth: (Float) -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        BrushControllerOptionSlider(
            sliderLabel = "StrokeWidth",
            sliderValue = initialValueStrokeWidth,
            minValue = 1f,
            maxValue = 100f,
            onValueChange = onValueChangeStrokeWidth
        )
        Spacer(modifier = Modifier.height(8.dp))
        BrushControllerOptionSlider(
            sliderLabel = "Opacity",
            sliderValue = initialValueOpacity,
            minValue = 1f,
            maxValue = 100f,
            onValueChange = onValueChangeOpacity
        )
    }
}

@Composable
fun BrushControllerOptionSlider(
    modifier: Modifier = Modifier,
    sliderLabel: String,
    sliderValue: Int,
    minValue: Float,
    maxValue: Float,
    onValueChange: (Float) -> Unit,
) {

    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Text(
                text = sliderLabel,
                modifier = Modifier
                    .weight(0.5f)
                    .padding(start = 32.dp),
                textAlign = TextAlign.Start
            )
            Text(
                text = sliderValue.toString(),
                modifier = Modifier
                    .weight(0.5f)
                    .padding(end = 32.dp),
                textAlign = TextAlign.End
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                modifier = Modifier
                    .weight(0.1f)
                    .clickable {
                        val decrementedValue = max(minValue.toInt(), sliderValue - 1)
                        onValueChange(decrementedValue.toFloat())
                    },
                imageVector = Icons.Default.Remove,
                contentDescription = null,
                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground)
            )

            Slider(
                modifier = Modifier.weight(0.8f),
                value = sliderValue.toFloat(),
                onValueChange = onValueChange,
                valueRange = minValue..maxValue,
            )


            Image(
                modifier = Modifier
                    .weight(0.1f)
                    .clickable {
                        val incrementedValue = min(maxValue.toInt(), sliderValue + 1)
                        onValueChange(incrementedValue.toFloat())
                    },
                imageVector = Icons.Default.Add,
                contentDescription = null,
                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PreviewControllerBS() {
    BrushControllerBottomSheet(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        initialValueOpacity = 10,
        onValueChangeOpacity = {},
        initialValueStrokeWidth = 2,
        onValueChangeStrokeWidth = {}
    )
}