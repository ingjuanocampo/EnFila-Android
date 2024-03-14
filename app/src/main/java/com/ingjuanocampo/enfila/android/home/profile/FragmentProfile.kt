package com.ingjuanocampo.enfila.android.home.profile

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.ingjuanocampo.common.composable.BaseComposableFragment
import com.ingjuanocampo.common.composable.LogoutOut
import com.ingjuanocampo.common.composable.MviBaseViewModel
import com.ingjuanocampo.common.composable.ViewEffect
import com.ingjuanocampo.enfila.android.home.profile.model.ProfileCard
import com.ingjuanocampo.enfila.android.home.profile.viewmodel.ProfileViewModel
import com.ingjuanocampo.enfila.domain.state.AppStateProvider
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FragmentProfile : BaseComposableFragment<ProfileCard>() {
    @Inject
    lateinit var stateProvider: AppStateProvider

    companion object {
        fun newInstance() = FragmentProfile()
    }

    override val viewModel: MviBaseViewModel<ProfileCard> by viewModels<ProfileViewModel>()

    @Composable
    override fun render(state: ProfileCard) {
        ProfileScreen()
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
