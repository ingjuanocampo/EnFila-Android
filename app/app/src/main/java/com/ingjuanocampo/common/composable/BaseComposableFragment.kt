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

abstract class BaseComposableFragment<STATE> : Fragment() {

    abstract val viewModel: MviBaseViewModel<STATE>

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

    @Composable
    abstract fun render(state: STATE)
}