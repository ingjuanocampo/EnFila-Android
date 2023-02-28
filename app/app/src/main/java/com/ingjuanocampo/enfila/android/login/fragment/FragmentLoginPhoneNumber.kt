package com.ingjuanocampo.enfila.android.login.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ingjuanocampo.enfila.android.R
import com.ingjuanocampo.enfila.android.login.viewmodel.LoginState
import com.ingjuanocampo.enfila.android.login.viewmodel.ViewModelLogin
import com.ingjuanocampo.enfila.domain.state.AppStateProvider
import com.ingjuanocampo.enfila.domain.usecases.signing.AuthState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FragmentLoginPhoneNumber : Fragment() {

    @Inject
    lateinit var stateProvider: AppStateProvider

    private lateinit var doVerificationButton: FloatingActionButton
    private val navController by lazy { NavHostFragment.findNavController(this) }

    val viewModel by viewModels<ViewModelLogin> (ownerProducer = { requireActivity() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.login_phone_number, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val phoneNumber = view.findViewById<EditText>(R.id.phoneNumber)

        phoneNumber.addTextChangedListener {
            viewModel.phoneNumber = (it.toString())
        }

        doVerificationButton = view.findViewById(R.id.floatButton)
        doVerificationButton.isEnabled = false
        doVerificationButton.setOnClickListener {
            viewModel.doLogin(requireActivity())
        }

        viewModel.state.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    LoginState.ToVerifyCode -> navController.navigate(R.id.action_fragmentLoginPhoneNumber_to_fragmentVerificationCode)
                    is LoginState.AuthenticationProcessState -> process(it.authState)
                    LoginState.NumberSet -> doVerificationButton.isEnabled = true
                }
            },
        )
    }

    private fun process(authState: AuthState) {
        when (authState) {
            AuthState.Authenticated -> stateProvider.provideCurrentState().navigateLaunchScreen()
            is AuthState.AuthError -> showToast("Error" + authState.e.toString())
        }
    }
}
