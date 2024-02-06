package com.ingjuanocampo.enfila.android.home.clients.details

import androidx.compose.runtime.Composable
import com.ingjuanocampo.common.composable.BaseComposableFragment
import com.ingjuanocampo.common.composable.MviBaseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentClientDetail: BaseComposableFragment<ClientUiDetail>() {
    override val viewModel: MviBaseViewModel<ClientUiDetail>
        get() = TODO("Not yet implemented")

    @Composable
    override fun render(state: ClientUiDetail) {
        TODO("Not yet implemented")
    }
}