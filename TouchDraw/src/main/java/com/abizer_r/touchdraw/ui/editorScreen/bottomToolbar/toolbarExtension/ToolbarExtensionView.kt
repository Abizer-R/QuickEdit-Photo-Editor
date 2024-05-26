import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abizer_r.components.R
import com.abizer_r.components.theme.SketchDraftTheme
import com.abizer_r.components.theme.ToolBarBackgroundColor
import com.abizer_r.touchdraw.ui.drawMode.drawingCanvas.drawingTool.shapes.ShapeType
import com.abizer_r.touchdraw.ui.editorScreen.bottomToolbar.toolbarExtension.CustomSliderItem
import com.abizer_r.touchdraw.utils.DrawingConstants

@Composable
fun ToolbarExtensionView(
    modifier: Modifier,
    width: Float? = null,
    onWidthChange: (Float) -> Unit = {},
    opacity: Float? = null,
    onOpacityChange: (Float) -> Unit = {},
    shapeType: ShapeType? = null,
    onShapeTypeChange: (ShapeType) -> Unit = {}
) {

    Column(
        modifier = modifier
            .background(ToolBarBackgroundColor)
            .padding(8.dp)
    ) {

        width?.let {
            CustomSliderItem(
                modifier = Modifier.padding(8.dp),
                sliderValue = width,
                sliderLabel = stringResource(id = R.string.width),
                minValue = DrawingConstants.MINIMUM_STROKE_WIDTH,
                maxValue = DrawingConstants.MAXIMUM_STROKE_WIDTH,
                onValueChange = onWidthChange
            )
        }

        opacity?.let {
            CustomSliderItem(
                modifier = Modifier.padding(8.dp),
                sliderValue = opacity,
                sliderLabel = stringResource(id = R.string.opacity),
                minValue = 0f,
                maxValue = 100f,
                onValueChange = onOpacityChange
            )
        }

        shapeType?.let {
            RadioButtonRow(
                modifier = Modifier.padding(8.dp),
                selectedShape = shapeType,
                onShapeSelected = onShapeTypeChange
            )
        }
    }
}

@Composable
fun RadioButtonRow(
    modifier: Modifier = Modifier,
    selectedShape: ShapeType,
    onShapeSelected: (ShapeType) -> Unit
) {
    val scrollState = rememberScrollState()
    Row(
        modifier = modifier.horizontalScroll(scrollState),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ShapeType.values().forEach { mShape ->
            RadioButton(
                selected = selectedShape == mShape,
                onClick = {
                    if (selectedShape != mShape) {
                        onShapeSelected(mShape)
                    }
                }
            )
            Text(
                text = mShape.name,
                modifier = Modifier.padding(end = 8.dp),
                style = TextStyle(color = MaterialTheme.colorScheme.onBackground)
            )
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewRadioRow() {
    RadioButtonRow(
        selectedShape = ShapeType.LINE,
        onShapeSelected = {}
    )
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewToolbarExtension() {
    SketchDraftTheme {
        ToolbarExtensionView(
            modifier = Modifier.fillMaxWidth(),
            width = 12f,
            onWidthChange = {},
            opacity = 45f,
            onOpacityChange = {},
            shapeType = ShapeType.LINE,
            onShapeTypeChange = {},
        )
    }
}