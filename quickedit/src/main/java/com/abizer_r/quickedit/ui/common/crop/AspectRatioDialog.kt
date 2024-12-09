package com.abizer_r.quickedit.ui.common.crop

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.abizer_r.quickedit.R
import com.abizer_r.quickedit.theme.DarkPanel
import com.abizer_r.quickedit.theme.QuickEditTheme
import com.abizer_r.quickedit.utils.defaultTextColor
import com.abizer_r.quickedit.utils.toast

const val MIN_RATIO = 0.15f
const val MAX_RATIO = 5.0f
@Composable
fun AspectRatioDialog(
    onDismiss: () -> Unit,
    onSetRatio: (Int, Int) -> Unit
) {

    val context = LocalContext.current

    Dialog(
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        ),
        onDismissRequest = onDismiss,
    ) {


        var aspectX by remember { mutableStateOf("1") }
        var aspectY by remember { mutableStateOf("1") }

        val titleTextStyle = MaterialTheme.typography.titleMedium.copy(
            color = defaultTextColor()
        )
        val bodyTextStyle = MaterialTheme.typography.bodySmall.copy(
            color = defaultTextColor()
        )

        Box(
            modifier = Modifier.background(
                color = DarkPanel,
                shape = RoundedCornerShape(10.dp)
            )
        ) {

            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.Center,
            ) {

                Text(
                    text = stringResource(R.string.enter_aspect_ratio),
                    style = titleTextStyle
                )
                Spacer(Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    RatioInputField(
                        modifier = Modifier.weight(1f),
                        text = aspectX,
                        onValueChange = { newValue -> aspectX = newValue },
                        labelText = stringResource(R.string.x),
                        bodyTextStyle = bodyTextStyle,
                    )
                    Spacer(Modifier.width(8.dp))
                    RatioInputField(
                        modifier = Modifier.weight(1f),
                        text = aspectY,
                        onValueChange = { newValue -> aspectY = newValue },
                        labelText = stringResource(R.string.y),
                        bodyTextStyle = bodyTextStyle,
                    )
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Aspect Ratio = ${getAspectRatioText(aspectX, aspectY)}",
                    style = bodyTextStyle
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Min Allowed Aspect Ratio = $MIN_RATIO",
                    style = bodyTextStyle
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Max Allowed Aspect Ratio = $MAX_RATIO",
                    style = bodyTextStyle
                )
                Spacer(Modifier.height(16.dp))
                Button(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = {
                        val validation = validateRatioItem(aspectX, aspectY)
                        if (validation.isValid) {
                            onSetRatio(aspectX.toInt(), aspectY.toInt())
                        } else {
                            context.toast(context.getString(validation.errorResId ?: R.string.something_went_wrong))
                        }
                    }
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = stringResource(R.string.select),
                        style = bodyTextStyle.copy(
                            color = MaterialTheme.colorScheme.background,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}

private fun getAspectRatioText(x: String, y: String): String {
    return getAspectRatio(x, y)?.toString() ?: "Invalid"
}

data class RatioItemValidationResult(
    val isValid: Boolean,
    @StringRes val errorResId: Int? = null
)

private fun validateRatioItem(x: String, y: String): RatioItemValidationResult {
    if (x.isBlank() || y.isBlank())
        return RatioItemValidationResult(false, R.string.fields_cannot_be_empty)
    val xF = x.toIntOrNull()
    val yF = y.toIntOrNull()
    if (xF == null || yF == null)
        return RatioItemValidationResult(false, R.string.not_a_valid_number)
    val ratio = (xF.toFloat() / yF.toFloat())
    return when {
        ratio < MIN_RATIO -> RatioItemValidationResult(false, R.string.ratio_lesser_than_minimum)
        ratio > MAX_RATIO -> RatioItemValidationResult(false, R.string.ratio_greater_than_minimum)
        else -> RatioItemValidationResult(true)
    }
}

private fun getAspectRatio(x: String, y: String): Float? {
    if (x.isBlank() || y.isBlank())
        return null
    val xF = x.toFloatOrNull() ?: return null
    val yF = y.toFloatOrNull() ?: return null
    return xF / yF
}

@Composable
private fun RatioInputField(
    modifier: Modifier,
    text: String,
//    onValueChange: (TextFieldValue) -> Unit,
    onValueChange: (String) -> Unit,
    labelText: String,
    bodyTextStyle: TextStyle
) {
    OutlinedTextField(
        modifier = modifier,
        singleLine = true,
//        value = TextFieldValue(
//            text = text,
//            selection = TextRange(text.length)
//        ),
//        onValueChange = onValueChange,
        value = text,
        onValueChange = { newValue ->
            // Allow only digits and empty value
            if (newValue.isEmpty() || newValue.all { it.isDigit() }) {
                onValueChange(newValue)
            }
        },
        textStyle = bodyTextStyle,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        leadingIcon = { Text(text = labelText, style = bodyTextStyle) }
    )
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewAspectRatioDialog() {
    QuickEditTheme {
        AspectRatioDialog(
            onDismiss = {},
            onSetRatio = { _, _ -> }
        )
    }
}