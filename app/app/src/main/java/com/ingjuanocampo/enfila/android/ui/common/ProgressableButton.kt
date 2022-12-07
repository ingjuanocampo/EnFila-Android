package com.ingjuanocampo.enfila.android.ui.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedComposeButton(
    modifier: Modifier = Modifier
        .size(40.dp),
    state: ProgressableButtonState = ProgressableButtonState.PROGRESS,
    content: @Composable() () -> Unit,
) {

    val contentStateVisibility = remember {
        MutableTransitionState(true).apply {
            // Start the animation immediately.
            targetState = false
        }
    }

    val progressStateVisibility = remember {
        MutableTransitionState(false).apply {
            // Start the animation immediately.
            targetState = true
        }
    }


    AnimatedVisibility(visibleState = contentStateVisibility) {
        content()
    }

    AnimatedVisibility(visibleState = progressStateVisibility) {
        CircularProgressIndicator(
            modifier = modifier,
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 3.dp
        )
    }


    when (state) {
        ProgressableButtonState.IDLE -> {
            contentStateVisibility.targetState = true
            progressStateVisibility.targetState = false
        }
        ProgressableButtonState.PROGRESS -> {
            contentStateVisibility.targetState = false
            progressStateVisibility.targetState = true
        }
    }

}

enum class ProgressableButtonState {
    IDLE,
    PROGRESS
}

@Composable
@Preview(
    device = Devices.PIXEL_4_XL,
    showBackground = true,
)
private fun ProgressableButtonPreview() {
    AnimatedComposeButton {
        Button(onClick = { /*TODO*/ }) {
            Text(text = "Test")
        }
    }
}