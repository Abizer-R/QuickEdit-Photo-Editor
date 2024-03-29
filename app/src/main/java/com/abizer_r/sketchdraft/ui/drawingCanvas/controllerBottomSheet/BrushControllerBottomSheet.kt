package com.abizer_r.sketchdraft.ui.drawingCanvas.controllerBottomSheet

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abizer_r.sketchdraft.ui.theme.SketchDraftTheme
import com.abizer_r.sketchdraft.util.AppUtils
import kotlin.math.max
import kotlin.math.min

@Composable
@ExperimentalMaterial3Api
fun BrushControllerBottomSheet(
    modifier: Modifier = Modifier,
    controllerBsState: ControllerBSState,
    onEvent: (ControllerBSEvents) -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        BrushControllerOptionSlider(
            sliderLabel = "StrokeWidth",
            sliderValue = controllerBsState.strokeWidth,
            minValue = 1f,
            maxValue = 100f,
            onValueChange = {
                onEvent(
                    ControllerBSEvents.StrokeWidthChanged(it)
                )
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        BrushControllerOptionSlider(
            sliderLabel = "Opacity",
            sliderValue = controllerBsState.opacity,
            minValue = 1f,
            maxValue = 100f,
            onValueChange = {
                onEvent(
                    ControllerBSEvents.OpacityChanged(it)
                )
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow {
            itemsIndexed(controllerBsState.colorList) { index, color ->
                ColorListItem(
                    color = color,
                    isSelected = controllerBsState.selectedColorIndex == index,
                    onColorClicked = {
                        onEvent(
                            ControllerBSEvents.ColorSelected(index)
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun ColorListItem(
    color: Color,
    isSelected: Boolean,
    onColorClicked: () -> Unit
) {
    if (isSelected) {
        Image(
            painter = ColorPainter(color),
            contentDescription = null,
            modifier = Modifier
                .padding(4.dp)
                .clip(CircleShape)
                .background(color = MaterialTheme.colorScheme.onBackground)
                .padding(4.dp)
                .clip(CircleShape)
                .size(32.dp)
                .clickable {
                    onColorClicked()
                }
        )

    } else {
        Image(
            painter = ColorPainter(color),
            contentDescription = null,
            modifier = Modifier
                .padding(8.dp)
                .size(32.dp)
                .clip(CircleShape)
                .clickable {
                    onColorClicked()
                }
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
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewControllerBS() {

    Surface {
        BrushControllerBottomSheet(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            controllerBsState = ControllerBSState(
                opacity = 80,
                strokeWidth = 6,
                colorList = AppUtils.colorList,
                selectedColorIndex = 0
            ),
            onEvent = {}
        )

    }
}