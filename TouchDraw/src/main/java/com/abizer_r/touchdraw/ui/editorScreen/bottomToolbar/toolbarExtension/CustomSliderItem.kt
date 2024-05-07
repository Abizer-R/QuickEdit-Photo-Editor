package com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.toolbarExtension

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.abizer_r.components.theme.SketchDraftTheme
import com.smarttoolfactory.slider.ColorfulSlider
import com.smarttoolfactory.slider.MaterialSliderDefaults
import com.smarttoolfactory.slider.SliderBrushColor
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

@Composable
fun CustomSliderItem(
    modifier: Modifier = Modifier,
    sliderLabel: String,
    sliderValue: Float,
    minValue: Float,
    maxValue: Float,
    onValueChange: (Float) -> Unit,
) {

    ConstraintLayout(modifier = modifier.fillMaxWidth()) {
        val (btnMinus, btnPlus, slider, txtLabel, txtCurrValue) = createRefs()

        Text(
            modifier = Modifier
                .constrainAs(txtLabel) {
                    start.linkTo(slider.start)
                    bottom.linkTo(slider.top)
                    top.linkTo(parent.top)
                }
                .padding(start = 8.dp),
            text = sliderLabel,
            textAlign = TextAlign.Start,
            style = TextStyle(
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = MaterialTheme.typography.labelMedium.fontSize
            )
        )
        Text(
            modifier = Modifier
                .constrainAs(txtCurrValue) {
                    end.linkTo(slider.end)
                    bottom.linkTo(slider.top)
                    top.linkTo(parent.top)
                }
                .padding(end = 8.dp),
            text = sliderValue.roundToInt().toString(),
            textAlign = TextAlign.End,
            style = TextStyle(
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = MaterialTheme.typography.labelMedium.fontSize
            )
        )

        Image(
            modifier = Modifier
                .constrainAs(btnMinus) {
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                }
                .padding(horizontal = 8.dp)
                .clickable {
                    val decrementedValue = max(minValue, sliderValue - 1)
                    onValueChange(decrementedValue)
                }
                .size(18.dp),
            imageVector = Icons.Default.Remove,
            contentDescription = null,
            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground)
        )

        ColorfulSlider(
            modifier = Modifier.constrainAs(slider) {
                start.linkTo(btnMinus.end)
                end.linkTo(btnPlus.start)
                top.linkTo(btnMinus.top)
                bottom.linkTo(btnMinus.bottom)
                width = Dimension.fillToConstraints
                height = Dimension.value(28.dp)
            },
            value = sliderValue,
            onValueChange = onValueChange,
            valueRange = minValue..maxValue,
            thumbRadius = 6.dp,
            trackHeight = 3.dp,
            colors = MaterialSliderDefaults.materialColors(
                thumbColor = SliderBrushColor(color = MaterialTheme.colorScheme.primary),
                activeTrackColor = SliderBrushColor(color = MaterialTheme.colorScheme.primary)
            )
        )
//        Slider(
//            modifier = Modifier.constrainAs(slider) {
//                start.linkTo(btnMinus.end)
//                end.linkTo(btnPlus.start)
//                top.linkTo(btnMinus.top)
//                bottom.linkTo(btnMinus.bottom)
//                width = Dimension.fillToConstraints
//                height = Dimension.value(28.dp)
//            },
//            value = sliderValue,
//            onValueChange = onValueChange,
//            valueRange = minValue..maxValue
//        )


        Image(
            modifier = Modifier
                .constrainAs(btnPlus) {
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
                .padding(horizontal = 8.dp)
                .clickable {
                    val incrementedValue = min(maxValue, sliderValue + 1)
                    onValueChange(incrementedValue)
                }
                .size(18.dp),
            imageVector = Icons.Default.Add,
            contentDescription = null,
            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground)
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewCustomSlider() {
    SketchDraftTheme {
        CustomSliderItem(
            modifier = Modifier.padding(12.dp),
            sliderLabel = "label",
            sliderValue = 45f,
            minValue = 2f,
            maxValue = 100f,
            onValueChange = {}
        )
    }
}