package com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SliderState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.abizer_r.components.theme.SketchDraftTheme
import kotlin.math.max
import kotlin.math.min

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomSliderItem(
    modifier: Modifier = Modifier,
    sliderLabel: String,
    sliderValue: Int,
    minValue: Float,
    maxValue: Float,
    onValueChange: (Float) -> Unit,
) {

    val interactionSource = remember {
        MutableInteractionSource()
    }
    val sliderState = remember {
        SliderState()
    }

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
                textAlign = TextAlign.Start,
                style = TextStyle(color = MaterialTheme.colorScheme.onBackground)
            )
            Text(
                text = sliderValue.toString(),
                modifier = Modifier
                    .weight(0.5f)
                    .padding(end = 32.dp),
                textAlign = TextAlign.End,
                style = TextStyle(color = MaterialTheme.colorScheme.onBackground)
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


            /**
             * TODO: TRY using the ColorfulSlider from SmartToolFactory
             * https://github.com/SmartToolFactory/Compose-Colorful-Sliders
             */



            Slider(
                modifier = Modifier.weight(0.8f),
                value = sliderValue.toFloat(),
                onValueChange = onValueChange,
                valueRange = minValue..maxValue,
                thumb = {
                    SliderDefaults.Thumb(
                        interactionSource = interactionSource,
                        thumbSize = DpSize(16.dp, 16.dp),
                    )
                },
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

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewCustomSlider() {
    SketchDraftTheme {
        CustomSliderItem(
            sliderLabel = "label",
            sliderValue = 45,
            minValue = 2f,
            maxValue = 100f
        ) {

        }
    }
}