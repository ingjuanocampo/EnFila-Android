package com.ingjuanocampo.enfila.android.login.new_account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.ingjuanocampo.enfila.android.R
import com.ingjuanocampo.enfila.android.login.new_account.viewmodel.ProfileState
import com.ingjuanocampo.enfila.android.login.new_account.viewmodel.ViewModelFragmentProfile
import com.ingjuanocampo.enfila.android.login.fragment.showToast
import com.ingjuanocampo.enfila.di.AppComponent
import com.ingjuanocampo.enfila.domain.state.AppStateProvider
import com.ingjuanocampo.enfila.domain.usecases.signing.AuthState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FragmentNewCompany : Fragment() {


    @Inject
    lateinit var stateProvider : AppStateProvider

    companion object {
        fun newInstance() = FragmentNewCompany()
        fun newInstance(phone: String, id : String) = FragmentNewCompany().apply {
            val arguments = Bundle()
            arguments.putString("phone", phone)
            arguments.putString("id", id)
            this.arguments = arguments
        }
    }

    private var phoneNumber: EditText? = null
    private val viewModel by viewModels<ViewModelFragmentProfile>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        phoneNumber = view.findViewById<EditText>(R.id.phoneNumber)
        val nameEd = view.findViewById<EditText>(R.id.nameEd)
        val companyName = view.findViewById<EditText>(R.id.companyName)
        val createOrUpdateBt = view.findViewById<Button>(R.id.createOrUpdateBt)
        createOrUpdateBt.setOnClickListener {
            viewModel.createUserAndLogin(nameEd.text.toString(), companyName.text.toString())
        }

        viewModel.init(requireArguments())

        viewModel.state.observe(viewLifecycleOwner, Observer{
            when(it) {
                is ProfileState.CreationFlow -> setPhoneAsBlocked(it.phone)
                is ProfileState.AuthProcess -> handleAuthProcess(it.authState)
            }
        })
    }

    private fun handleAuthProcess(authState: AuthState) {
        when (authState) {
            AuthState.Authenticated -> stateProvider.provideCurrentState().navigateLaunchScreen()
            is AuthState.AuthError -> showToast("Error, something when wrong")
        }
    }

    private fun setPhoneAsBlocked(phone: String) {
        phoneNumber?.setText(phone)
        phoneNumber?.isEnabled = false
    }

}