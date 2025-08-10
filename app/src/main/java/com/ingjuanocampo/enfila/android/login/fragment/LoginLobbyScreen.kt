package com.ingjuanocampo.enfila.android.login.fragment

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ingjuanocampo.enfila.android.R
import com.ingjuanocampo.enfila.android.login.viewmodel.ViewModelLoginLobby
import com.ingjuanocampo.enfila.android.ui.theme.AppTheme
import com.ingjuanocampo.enfila.android.ui.theme.ButtonPrimary
import com.ingjuanocampo.enfila.android.ui.theme.ButtonPrimaryStroke

@Composable
fun LoginLobbyScreen(
    onPhoneLogin: () -> Unit,
    viewModel: ViewModelLoginLobby
) {
    AppTheme {
        val context = LocalContext.current

        val launcher =
            rememberLauncherForActivityResult(
                ActivityResultContracts.StartIntentSenderForResult()
            ) { result ->
                viewModel.processResults(result, context)
            }

        Column(
            modifier =
            Modifier
                .fillMaxSize()
                // .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(bottom = 40.dp),
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            // Create a scope that is automatically cancelled
            // if the user closes your app while async work is
            // happening
            val scope = rememberCoroutineScope()
            ButtonPrimary(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    viewModel.signIn(
                        context = context,
                        launcher = launcher
                    )
                },
                text = "Google Sign in"
            )

            Spacer(modifier = Modifier.size(10.dp))
            ButtonPrimaryStroke(
                modifier = Modifier.fillMaxWidth(),
                onClick = onPhoneLogin,
                text = "Login with phone"
            )
        }
    }
}

/*@Preview
@Composable
fun Preview() {
    LoginLobbyScreen({

    })
}*/
