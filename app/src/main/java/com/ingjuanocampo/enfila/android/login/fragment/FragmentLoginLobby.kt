package com.ingjuanocampo.enfila.android.login.fragment

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.ingjuanocampo.common.composable.BaseComposableFragment
import com.ingjuanocampo.common.composable.MviBaseViewModel
import com.ingjuanocampo.common.composable.ViewEffect
import com.ingjuanocampo.enfila.android.R
import com.ingjuanocampo.enfila.android.login.viewmodel.ViewModelLoginLobby
import com.ingjuanocampo.enfila.domain.state.AppStateProvider
import com.ingjuanocampo.enfila.domain.usecases.signing.AuthState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FragmentLoginLobby : BaseComposableFragment<Boolean>() {

    private val viewModelLobby: ViewModelLoginLobby by viewModels()
    @Inject
    lateinit var stateProvider: AppStateProvider
    override val viewModel: MviBaseViewModel<Boolean>
        get() = viewModelLobby

    @Composable
    override fun render(state: Boolean) {
        LoginLobbyScreen(
            onPhoneLogin = {
                navController.navigate(R.id.action_fragmentLoginLobby_to_fragmentLoginPhoneNumber)
            },
            viewModelLobby
        )
    }

    override fun onNewViewEffect(viewEffect: ViewEffect) {
        super.onNewViewEffect(viewEffect)
        viewEffect as AuthState
        when (viewEffect) {
            is AuthState.AuthError -> {
                showToast("Error logging" )
            }
            AuthState.Authenticated -> stateProvider.provideCurrentState()
                .navigateLaunchScreen(requireActivity())
            is AuthState.NewAccount -> navController.navigate(
                R.id.action_fragmentVerificationCode_to_fragmentProfile,
                Bundle().apply {
                    putString("id", viewEffect.id)
                },
            )
        }
    }
}