package com.ingjuanocampo.enfila.android.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableStateFlow


val HourlyShapes: Shapes
    @Composable
    get() = Shapes(
        extraSmall = RoundedCornerShape(4.dp),
        small = RoundedCornerShape(8.dp),
        medium = RoundedCornerShape(12.dp),
        large = RoundedCornerShape(16.dp),
        extraLarge = RoundedCornerShape(30.dp)
    )


@Composable
fun ButtonPrimary(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
    disabledContainerColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    disabledContentColor: Color = MaterialTheme.colorScheme.onPrimary,
    enableState: MutableStateFlow<Boolean> = MutableStateFlow(true)
) {
    val isEnable by enableState.collectAsState()
    Button(
        modifier = modifier.height(44.dp),
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(
            containerColor,
            contentColor,
            disabledContainerColor,
            disabledContentColor
        ),
        onClick = onClick,
        enabled = isEnable
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.copy(color = contentColor, fontWeight = FontWeight.Medium)
        )
    }
}


@Composable
fun ButtonGrayStroke(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier
) = ButtonPrimaryStroke(onClick, text, modifier, MaterialTheme.colorScheme.onSurface)

@Composable
fun ButtonPrimaryStroke(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.primary
) =
    OutlinedButton(
        modifier = modifier.height(44.dp),
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(1.dp, containerColor),
        onClick = onClick
    ) {
        Text(
            text = text,
            style =  MaterialTheme.typography.bodyMedium.copy(color = containerColor, fontWeight = FontWeight.Medium)
        )
    }


@Composable
fun ButtonSecondary(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
) = ButtonPrimary(
    onClick,
    text,
    modifier = modifier,
    containerColor = MaterialTheme.colorScheme.secondaryContainer,
    contentColor = MaterialTheme.colorScheme.onSecondary
)

@Composable
fun ButtonTertiary(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
) = ButtonPrimary(
    onClick,
    text,
    modifier = modifier,
    containerColor = MaterialTheme.colorScheme.tertiary,
    contentColor = MaterialTheme.colorScheme.onTertiary
)

@Composable
fun ButtonError(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
) = ButtonPrimary(
    onClick,
    text,
    modifier = modifier,
    containerColor = MaterialTheme.colorScheme.error,
    contentColor = MaterialTheme.colorScheme.onError
)

@Composable
fun ButtonSurface(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
) = ButtonPrimary(
    onClick,
    text,
    modifier = modifier,
    containerColor = MaterialTheme.colorScheme.onSurface,
    contentColor = MaterialTheme.colorScheme.onPrimary
)

@Composable
fun ButtonGreyLight(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
) = ButtonPrimary(
    onClick,
    text,
    modifier = modifier,
    containerColor = MaterialTheme.colorScheme.surface,
    contentColor = MaterialTheme.colorScheme.onSurface
)

@Composable
fun ButtonContainer() {
    Column {
        val modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
        ButtonPrimary(
            {

            }, "ButtonPrimary",
            modifier
        )

        ButtonGrayStroke(
            {

            }, "ButtonGrayStroke",
            modifier
        )

        ButtonPrimaryStroke(
            {

            }, "ButtonPrimaryStroke",
            modifier
        )

        ButtonGreyLight(
            {

            }, "ButtonGreyLight",
            modifier
        )

        ButtonSecondary(
            {

            }, "ButtonSecondary",
            modifier
        )

        ButtonTertiary(
            {

            }, "ButtonTertiary",
            modifier
        )

        ButtonSurface(
            {

            }, "ButtonSurface",
            modifier
        )

        ButtonError(
            {

            }, "ButtonError",
            modifier
        )
    }
}

@Preview(
    device = Devices.PIXEL_4,
    backgroundColor = 0XFFFFFF,
    showBackground = true
)
@Composable
fun PreviewButtons() {
    AppTheme {
        ButtonContainer()
    }

}

@Preview(
    device = Devices.PIXEL_4,
    backgroundColor = 0XFF000000,
    showBackground = true
)
@Composable
fun PreviewButtonsDark() {
    AppTheme(useDarkTheme = true) {
        ButtonContainer()
    }

}








