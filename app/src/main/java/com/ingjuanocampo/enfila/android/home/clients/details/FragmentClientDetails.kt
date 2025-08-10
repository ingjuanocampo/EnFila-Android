package com.ingjuanocampo.enfila.android.home.clients.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ingjuanocampo.common.composable.NavigationEffect
import com.ingjuanocampo.enfila.android.utils.navigateToCustomDest
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentClientDetails : Fragment() {
    private val viewModel: ClientDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val state by viewModel.state.collectAsState()
                ClientDetailsScreen(
                    state = state,
                    onShiftClick = viewModel::onShiftClicked,
                    onRefresh = viewModel::onRefresh,
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeEvents()
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        // Get client ID from arguments and load data
        val clientId = arguments?.getString("clientId")
        if (clientId != null) {
            viewModel.loadClientDetails(clientId)
        } else {
            // Handle error - no client ID provided
            // Could navigate back or show error
        }
    }

    private fun observeEvents() =
        lifecycleScope.launch {
            viewModel.event.onEach { viewEffect ->
                if (viewEffect is NavigationEffect) {
                    findNavController().navigateToCustomDest(viewEffect)
                }
            }.launchIn(this)
        }
}
