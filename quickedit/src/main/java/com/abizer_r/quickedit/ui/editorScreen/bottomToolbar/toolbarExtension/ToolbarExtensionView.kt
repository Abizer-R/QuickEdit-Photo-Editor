import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abizer_r.quickedit.R
import com.abizer_r.quickedit.theme.QuickEditTheme
import com.abizer_r.quickedit.theme.ToolBarBackgroundColor
import com.abizer_r.quickedit.ui.drawMode.drawingCanvas.drawingTool.shapes.ShapeType
import com.abizer_r.quickedit.ui.editorScreen.bottomToolbar.toolbarExtension.CustomSliderItem
import com.abizer_r.quickedit.utils.drawMode.DrawingConstants

@Composable
fun ToolbarExtensionView(
    modifier: Modifier,
    showSeparationAtBottom: Boolean = true,
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
            .padding(horizontal = 8.dp) // added Spacer for vertical padding
    ) {

        Spacer(modifier = Modifier.fillMaxWidth().height(8.dp))

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


        Spacer(modifier = Modifier.fillMaxWidth().height(8.dp))

        if (showSeparationAtBottom) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .height(1.dp)
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
            val onClickLambda = remember<() -> Unit> {{
                if (selectedShape != mShape) {
                    onShapeSelected(mShape)
                }
            }}
            RadioButton(
                selected = selectedShape == mShape,
                onClick = onClickLambda
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
    QuickEditTheme {
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