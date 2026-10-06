package com.ingjuanocampo.enfila.android.home.tips

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.ingjuanocampo.common.composable.BaseComposableFragment
import com.ingjuanocampo.common.composable.MviBaseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentTips : BaseComposableFragment<TipsViewState>() {
    companion object {
        fun newInstance() = FragmentTips()
    }

    override val viewModel: MviBaseViewModel<TipsViewState> by viewModels<ViewModelTips>()

    @Composable
    override fun render(state: TipsViewState) {
        TipsScreen(
            state = state,
            onRetry = { (viewModel as ViewModelTips).loadTips() },
        )
    }

    override fun onResume() {
        super.onResume()
        (viewModel as ViewModelTips).loadTips()
    }
}
