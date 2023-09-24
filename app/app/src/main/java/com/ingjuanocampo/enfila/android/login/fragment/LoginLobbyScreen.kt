package com.ingjuanocampo.enfila.android.login.fragment

import android.content.Context
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
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
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.ingjuanocampo.enfila.android.R
import com.ingjuanocampo.enfila.android.login.viewmodel.ViewModelLoginLobby
import com.ingjuanocampo.enfila.android.ui.theme.AppTheme
import com.ingjuanocampo.enfila.android.ui.theme.ButtonPrimary
import com.ingjuanocampo.enfila.android.ui.theme.ButtonPrimaryStroke
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun LoginLobbyScreen(onPhoneLogin: () -> Unit,
                     viewModel: ViewModelLoginLobby) {
    AppTheme {
        val context = LocalContext.current

        val launcher = rememberLauncherForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->
            viewModel.processResults(result, context)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                //.background(MaterialTheme.colorScheme.primaryContainer)
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
                    scope.launch {
                        signIn(
                            context = context,
                            launcher = launcher
                        )
                    }
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

suspend fun signIn(
    context: Context,
    launcher: ActivityResultLauncher<IntentSenderRequest>
) {
    val oneTapClient = Identity.getSignInClient(context)
    val signInRequest = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                // Your server's client ID, not your Android client ID.
                .setServerClientId("589084343729-6iointridcnan0bb6aqmpmdaersjm303.apps.googleusercontent.com")
                // Only show accounts previously used to sign in.
                .setFilterByAuthorizedAccounts(false)
                .build()
        )
        // Automatically sign in when exactly one credential is retrieved.
        .setAutoSelectEnabled(true)
        .build()

    try {
        // Use await() from https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-play-services
        // Instead of listeners that aren't cleaned up automatically
        val result = oneTapClient.beginSignIn(signInRequest).await()

        // Now construct the IntentSenderRequest the launcher requires
        val intentSenderRequest = IntentSenderRequest.Builder(result.pendingIntent).build()
        launcher.launch(intentSenderRequest)
    } catch (e: Exception) {
        // No saved credentials found. Launch the One Tap sign-up flow, or
        // do nothing and continue presenting the signed-out UI.
        Log.d("LOG", e.message.toString())
    }
}

