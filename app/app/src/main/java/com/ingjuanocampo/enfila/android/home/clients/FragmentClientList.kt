package com.ingjuanocampo.enfila.android.home.clients

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.ingjuanocampo.common.composable.BaseComposableFragment
import com.ingjuanocampo.common.composable.MviBaseViewModel
import com.ingjuanocampo.enfila.android.home.clients.viewmodel.ClientsViewState
import com.ingjuanocampo.enfila.android.home.clients.viewmodel.ViewModelClientList
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentClientList : BaseComposableFragment<ClientsViewState>() {

    override val viewModel: MviBaseViewModel<ClientsViewState> by viewModels<ViewModelClientList>()

    @Composable
    override fun render(state: ClientsViewState) {
        when (state) {
            is ClientsViewState.DataLoaded -> ClientListScreenChat(clientList = state.clients) {
                (viewModel as ViewModelClientList).onSearch(it)
            }
        }

    }

    companion object {
        fun newInstance() = FragmentClientList()
    }

}