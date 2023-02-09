package com.ingjuanocampo.enfila.android.login.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ingjuanocampo.enfila.android.R
import com.ingjuanocampo.enfila.android.login.viewmodel.LoginState
import com.ingjuanocampo.enfila.android.login.viewmodel.ViewModelLogin
import com.ingjuanocampo.enfila.di.AppComponent
import com.ingjuanocampo.enfila.domain.state.AppStateProvider
import com.ingjuanocampo.enfila.domain.usecases.signing.AuthState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FragmentVerificationCode : Fragment() {

    @Inject
    lateinit var stateProvider: AppStateProvider
    val viewModel by viewModels<ViewModelLogin> (ownerProducer = { requireActivity() })
    private val navController by lazy { NavHostFragment.findNavController(this) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.verification_code, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val verificationCode = view.findViewById<EditText>(R.id.verificationCode)

        verificationCode.addTextChangedListener {
            viewModel.verificationCode = (it.toString())
        }

        viewModel.state.observe(viewLifecycleOwner, Observer {
            when(it) {
                is LoginState.AuthenticationProcessState ->
                    when (it.authState) {
                    AuthState.Authenticated ->  stateProvider.provideCurrentState().navigateLaunchScreen()
                    is AuthState.NewAccount -> navController.navigate(R.id.action_fragmentVerificationCode_to_fragmentProfile, Bundle().apply {
                        putString("phone", viewModel.phoneNumber)
                        putString("id", it.authState.id)
                    })
                    is AuthState.AuthError -> showToast("Error" + it.authState.e.toString())
                }
            }
        })


        var doVerificationButton = view.findViewById<FloatingActionButton>(R.id.floatButton)
        doVerificationButton.setOnClickListener {
            viewModel.verify()
        }


    }
}

fun Fragment.showToast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}