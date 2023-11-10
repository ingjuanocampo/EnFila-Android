package com.ingjuanocampo.common.composable

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.ingjuanocampo.enfila.android.utils.navigateToCustomDest
import com.ingjuanocampo.enfila.domain.state.AppStateProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

abstract class BaseComposableFragment<STATE> : Fragment() {

    abstract val viewModel: MviBaseViewModel<STATE>
    protected val navController by lazy { NavHostFragment.findNavController(this) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val state by viewModel.state.collectAsState()
                render(state)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeEvents()
    }

    private fun observeEvents() = lifecycleScope.launch {
        viewModel.event.onEach {
            when (it) {

                is FinishEffect -> {
                    requireActivity().finish()
                }
                is BackEffect -> {
                    requireActivity().onBackPressed()
                }
                is InvalidateMenuOptions -> {
                    requireActivity().invalidateOptionsMenu()
                }
                is NavigationEffect -> {
                    navController.navigateToCustomDest(it)
                }
                else -> {
                    withContext(Dispatchers.Main) {
                        onNewViewEffect(it)
                    }
                }
            }
        }.launchIn(this)
    }

    open fun onNewViewEffect(viewEffect: ViewEffect) {
        // Override when required
    }

    @Composable
    abstract fun render(state: STATE)
}