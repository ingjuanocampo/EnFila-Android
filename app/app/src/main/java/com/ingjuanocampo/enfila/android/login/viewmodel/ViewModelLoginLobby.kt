package com.ingjuanocampo.enfila.android.login.viewmodel

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.ingjuanocampo.common.composable.MviBaseViewModel
import com.ingjuanocampo.common.composable.ShowErrorDialogEffect
import com.ingjuanocampo.enfila.android.utils.launchGeneral
import com.ingjuanocampo.enfila.android.utils.launchGeneralWithErrorHandling
import com.ingjuanocampo.enfila.domain.usecases.signing.SignInUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.tasks.await
import java.io.Serializable
import javax.inject.Inject

@HiltViewModel
class ViewModelLoginLobby @Inject constructor(
    private val signUC: SignInUC,
) : MviBaseViewModel<Boolean>(true) {

    private val auth by lazy { FirebaseAuth.getInstance() }


    fun signIn(
        context: Context,
        launcher: ActivityResultLauncher<IntentSenderRequest>
    ) = launchGeneralWithErrorHandling {
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


            // Use await() from https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-play-services
            // Instead of listeners that aren't cleaned up automatically
            val result = oneTapClient.beginSignIn(signInRequest).await()

            // Now construct the IntentSenderRequest the launcher requires
            val intentSenderRequest = IntentSenderRequest.Builder(result.pendingIntent).build()
            launcher.launch(intentSenderRequest)

    }

    fun processResults(result: ActivityResult, context: Context) {
        launchGeneralWithErrorHandling {
            if (result.resultCode != Activity.RESULT_OK) {
                // The user cancelled the login, was it due to an Exception?
                if (result.data?.action == ActivityResultContracts.StartIntentSenderForResult.ACTION_INTENT_SENDER_REQUEST) {
                    val exception: Serializable? = result.data?.getSerializableExtra(
                        ActivityResultContracts.StartIntentSenderForResult.EXTRA_SEND_INTENT_EXCEPTION
                    )
                    Log.e("LOG", "Couldn't start One Tap UI: ${exception?.toString()}")
                }
                return@launchGeneralWithErrorHandling
            }
            val oneTapClient = Identity.getSignInClient(context)
            val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
            val idToken = credential.googleIdToken
            if (idToken != null) {
                // Got an ID token from Google. Use it to authenticate
                // with your backend.
                onVerification(credential)
            } else {
                Log.d("LOG", "Null Token") // handle error
            }
        }

    }

    private suspend fun onVerification(googleCredential: SignInCredential) {
        val idToken = googleCredential.googleIdToken
        when {
            idToken != null -> {
                // Got an ID token from Google. Use it to authenticate
                // with Firebase.
                val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                auth.signInWithCredential(firebaseCredential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            completeSignInProcess(googleCredential)
                            // Sign in success, update UI with the signed-in user's information
                            /* Log.d(TAG, "signInWithCredential:success")
                             val user = auth.currentUser
                             updateUI(user)*/
                        } else {
                            // If sign in fails, display a message to the user.
                            /*       Log.w(TAG, "signInWithCredential:failure", task.exception)
                                   updateUI(null)*/
                        }
                    }
            }

            else -> {
                // handle error
                // Shouldn't happen.
                //Log.d(TAG, "No ID token!")
            }
        }
    }

    private fun completeSignInProcess(googleCredential: SignInCredential) {
        launchGeneralWithErrorHandling {
            signUC(googleCredential.googleIdToken!!).collect {
                _event.emit(it) // Connect this in the fragment, check for failure scenarios and test 
            }
        }

    }

}