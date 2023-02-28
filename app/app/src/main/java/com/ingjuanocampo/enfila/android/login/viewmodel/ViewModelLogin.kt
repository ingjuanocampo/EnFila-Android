package com.ingjuanocampo.enfila.android.login.viewmodel

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.ingjuanocampo.enfila.android.utils.launchGeneral
import com.ingjuanocampo.enfila.domain.usecases.signing.AuthState
import com.ingjuanocampo.enfila.domain.usecases.signing.SignInUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class ViewModelLogin @Inject constructor(
    private val signUC: SignInUC,
) : ViewModel() {

    var verificationCode: String? = null
    private var verificationId: String? = null
    val state = MutableLiveData<LoginState>()
    var activity: Activity? = null

    var phoneNumber: String = ""
        set(value) {
            if (value.isNotEmpty() && value.count() == 10) {
                field = value
                state.value = LoginState.NumberSet
            }
        }

    private val auth by lazy { FirebaseAuth.getInstance() }

    private val authCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(p0: FirebaseException) {
            state.value = LoginState.AuthenticationProcessState(AuthState.AuthError(p0))
        }

        override fun onCodeSent(
            _verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken,
        ) {
            super.onCodeSent(_verificationId, token)
            verificationId = _verificationId
            state.value = LoginState.ToVerifyCode
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(activity!!) { task ->
                completeSignFlow(task)
            }
    }

    private fun completeSignFlow(task: Task<AuthResult>) = viewModelScope.launchGeneral {
        if (task.isSuccessful) {
            val user = task.result?.user
            if (user != null) {
                withContext(Dispatchers.IO) {
                    signUC(user.uid).collect {
                        state.postValue(LoginState.AuthenticationProcessState(it))
                    }
                }
            } else {
                state.postValue(
                    LoginState.AuthenticationProcessState(
                        AuthState.AuthError(
                            task.exception ?: Exception("No login"),
                        ),
                    ),
                )
            }
        } else {
            if (task.exception is FirebaseAuthInvalidCredentialsException) {
                // The verification code entered was invalid
            }
            state.postValue(
                LoginState.AuthenticationProcessState(
                    AuthState.AuthError(
                        task.exception ?: Exception("No login"),
                    ),
                ),
            )
            // Update UI
        }
    }

    fun doLogin(activity: Activity) {
        this.activity = activity

        viewModelScope.launchGeneral {
            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber("+57$phoneNumber") // /.setPhoneNumber("+16505553434")//      // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(activity) // Activity (for callback binding)
                .setCallbacks(authCallbacks) // OnVerificationStateChangedCallbacks
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
        }
    }

    fun verify() {
        viewModelScope.launchGeneral {
            val credential = PhoneAuthProvider.getCredential(verificationId!!, verificationCode!!)
            signInWithPhoneAuthCredential(credential)
        }
    }
}
