package com.ingjuanocampo.enfila.android.ui.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedComposeButton(
    modifier: Modifier = Modifier,
    state: AnimatedButtonState = AnimatedButtonState.IDLE,
    content:
        @Composable()
        () -> Unit
) {
    Box(
        modifier,
        contentAlignment = Alignment.Center
    ) {
        val contentStateVisibility =
            remember {
                MutableTransitionState(true)
            }

        val progressStateVisibility =
            remember {
                MutableTransitionState(false)
            }

        val animationSprint = 700f

        AnimatedVisibility(
            visibleState = progressStateVisibility,
            enter = fadeIn(spring(stiffness = animationSprint)) + scaleIn(spring(stiffness = animationSprint)),
            exit = fadeOut(spring(stiffness = animationSprint)) + scaleOut(spring(stiffness = animationSprint + animationSprint / 2))
        ) {
            CircularProgressIndicator(
                modifier =
                Modifier
                    .padding(2.dp)
                    .size(30.dp),
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 3.dp
            )
        }

        AnimatedVisibility(
            visibleState = contentStateVisibility,
            enter = fadeIn(spring(stiffness = animationSprint + animationSprint / 2)) + scaleIn(spring(stiffness = animationSprint + animationSprint / 2)),
            exit = fadeOut(spring(stiffness = animationSprint)) + scaleOut(spring(stiffness = animationSprint))
        ) {
            content()
        }

        when (state) {
            AnimatedButtonState.IDLE -> {
                contentStateVisibility.targetState = true
                progressStateVisibility.targetState = false
            }
            AnimatedButtonState.PROGRESS -> {
                contentStateVisibility.targetState = false
                progressStateVisibility.targetState = true
            }
        }
    }
}

enum class AnimatedButtonState {
    IDLE,
    PROGRESS
}

fun Boolean.toLoadingState(): AnimatedButtonState {
    return if (this) {
        AnimatedButtonState.PROGRESS
    } else {
        AnimatedButtonState.IDLE
    }
}

@Composable
@Preview(
    device = Devices.PIXEL_4_XL,
    showBackground = true
)
private fun ProgressableButtonPreview() {
    Column(Modifier.fillMaxWidth()) {
        AnimatedComposeButton(state = AnimatedButtonState.PROGRESS) {
            Button(onClick = { /*TODO*/ }) {
                Text(text = "Test")
            }
        }

        AnimatedComposeButton(state = AnimatedButtonState.PROGRESS) {
            Button(onClick = { /*TODO*/ }) {
                Text(text = "Test")
            }
        }
    }
}
