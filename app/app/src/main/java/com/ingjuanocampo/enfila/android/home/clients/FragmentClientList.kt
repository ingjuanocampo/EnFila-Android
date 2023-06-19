package com.ingjuanocampo.enfila.android.home.clients

import androidx.compose.runtime.Composable
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.NavHostFragment
import com.ingjuanocampo.common.composable.BaseComposableFragment
import com.ingjuanocampo.common.composable.MviBaseViewModel
import com.ingjuanocampo.enfila.android.R
import com.ingjuanocampo.enfila.android.home.clients.viewmodel.ClientsViewState
import com.ingjuanocampo.enfila.android.home.clients.viewmodel.ViewModelClientList
import com.ingjuanocampo.enfila.android.home.list.FragmentListItems
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FragmentClientList : BaseComposableFragment<ClientsViewState>() {

    private val navController by lazy { NavHostFragment.findNavController(this) }

    override val viewModel: MviBaseViewModel<ClientsViewState> by viewModels<ViewModelClientList>()

    @Composable
    override fun render(state: ClientsViewState) {
        when (state) {
            is ClientsViewState.DataLoaded -> ClientListScreenChat(clientList = state.clients, {
                (viewModel as ViewModelClientList).onSearch(it)
            }, {
                val navigator: FragmentNavigator =
                    navController.navigatorProvider.getNavigator(FragmentNavigator::class.java)

                val destination: FragmentNavigator.Destination = navigator.createDestination()
                    .setClassName(FragmentListItems::class.qualifiedName.orEmpty())
                destination.id = R.id.new_fragment
                destination.label = "Shifts for the selected client"

                navController.graph.addDestination(destination)

                val bundle = bundleOf("ClientId" to it)
                navController.navigate(R.id.new_fragment, bundle)
            })
        }

    }

    companion object {
        fun newInstance() = FragmentClientList()
    }

}