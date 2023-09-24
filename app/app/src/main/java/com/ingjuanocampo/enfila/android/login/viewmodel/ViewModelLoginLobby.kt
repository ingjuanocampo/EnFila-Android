package com.ingjuanocampo.enfila.android.login.viewmodel

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.ingjuanocampo.common.composable.MviBaseViewModel
import com.ingjuanocampo.enfila.android.utils.launchGeneral
import com.ingjuanocampo.enfila.domain.usecases.signing.SignInUC
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.Serializable
import javax.inject.Inject

@HiltViewModel
class ViewModelLoginLobby @Inject constructor(
    private val signUC: SignInUC,
    ): MviBaseViewModel<Boolean>(true) {

    private val auth by lazy { FirebaseAuth.getInstance() }
    fun processResults(result: ActivityResult, context: Context) {
        launchGeneral {
            if (result.resultCode != Activity.RESULT_OK) {
                // The user cancelled the login, was it due to an Exception?
                if (result.data?.action == ActivityResultContracts.StartIntentSenderForResult.ACTION_INTENT_SENDER_REQUEST) {
                    val exception: Serializable? = result.data?.getSerializableExtra(
                        ActivityResultContracts.StartIntentSenderForResult.EXTRA_SEND_INTENT_EXCEPTION
                    )
                    Log.e("LOG", "Couldn't start One Tap UI: ${exception?.toString()}")
                }
                return@launchGeneral
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
        launchGeneral {
            signUC(googleCredential.googleIdToken!!).collect {
                _event.emit(it) // Connect this in the fragment, check for failure scenarios and test 
            }
        }

    }

}