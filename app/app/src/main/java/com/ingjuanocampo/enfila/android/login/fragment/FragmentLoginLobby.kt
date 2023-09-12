package com.ingjuanocampo.enfila.android.login.fragment

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.ingjuanocampo.common.composable.BaseComposableFragment
import com.ingjuanocampo.common.composable.MviBaseViewModel
import com.ingjuanocampo.enfila.android.R
import com.ingjuanocampo.enfila.android.login.viewmodel.ViewModelLoginLobby

class FragmentLoginLobby: BaseComposableFragment<Boolean>() {

    val viewModelLobby : ViewModelLoginLobby by viewModels()

    override val viewModel: MviBaseViewModel<Boolean>
        get() = viewModelLobby

    @Composable
    override fun render(state: Boolean) {
        LoginLobbyScreen(onPhoneLogin = {
            navController.navigate(R.id.action_fragmentLoginLobby_to_fragmentLoginPhoneNumber)
        })
    }
}