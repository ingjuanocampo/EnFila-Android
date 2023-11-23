package com.ingjuanocampo.enfila.android.home.clients

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.Composable
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.NavHostFragment
import com.ingjuanocampo.common.composable.BaseComposableFragment
import com.ingjuanocampo.common.composable.GenericEmptyState
import com.ingjuanocampo.common.composable.MviBaseViewModel
import com.ingjuanocampo.enfila.android.R
import com.ingjuanocampo.enfila.android.home.clients.viewmodel.ClientsViewState
import com.ingjuanocampo.enfila.android.home.clients.viewmodel.ViewModelClientList
import com.ingjuanocampo.enfila.android.home.list.FragmentListItems
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FragmentClientList : BaseComposableFragment<ClientsViewState>() {

    override val viewModel: MviBaseViewModel<ClientsViewState> by viewModels<ViewModelClientList>()

    @Composable
    override fun render(state: ClientsViewState) {
        when (state) {
            is ClientsViewState.DataLoaded -> ClientListScreenChat(clientList = state.clients, {
                (viewModel as ViewModelClientList).onSearch(it)
            }, {
                (viewModel as ViewModelClientList).onClientSelected(it)
            })

            else -> {}
        }

    }

    companion object {
        fun newInstance() = FragmentClientList()
    }

}