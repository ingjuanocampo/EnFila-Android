package com.ingjuanocampo.enfila.android.home.account

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.ingjuanocampo.common.composable.BaseComposableFragment
import com.ingjuanocampo.common.composable.LogoutOut
import com.ingjuanocampo.common.composable.MviBaseViewModel
import com.ingjuanocampo.common.composable.ViewEffect
import com.ingjuanocampo.enfila.android.home.account.model.AccountCard
import com.ingjuanocampo.enfila.android.home.account.viewmodel.AccountViewModel
import com.ingjuanocampo.enfila.domain.state.AppStateProvider
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FragmentAccount : BaseComposableFragment<AccountCard>() {
    @Inject
    lateinit var stateProvider: AppStateProvider

    companion object {
        fun newInstance() = FragmentAccount()
    }

    override val viewModel: MviBaseViewModel<AccountCard> by viewModels<AccountViewModel>()

    @Composable
    override fun render(state: AccountCard) {
        AccountScreen()
    }

    override fun onNewViewEffect(viewEffect: ViewEffect) {
        super.onNewViewEffect(viewEffect)
        when (viewEffect) {
            is LogoutOut -> {
                stateProvider.provideCurrentState().navigateLaunchScreen(requireActivity())
            }
        }
    }
}
