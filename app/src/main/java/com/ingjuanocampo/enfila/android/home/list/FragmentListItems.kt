package com.ingjuanocampo.enfila.android.home.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.List
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import com.ingjuanocampo.cdapter.CompositeDelegateAdapter
import com.ingjuanocampo.common.composable.GenericEmptyState
import com.ingjuanocampo.enfila.android.R
import com.ingjuanocampo.enfila.android.databinding.FragmentListItemsBinding
import com.ingjuanocampo.enfila.android.home.list.adapter.DelegateShift
import com.ingjuanocampo.enfila.android.home.list.viewmodel.ViewModelListItems
import com.ingjuanocampo.enfila.android.navigation.NavigationDestinations
import com.ingjuanocampo.enfila.android.utils.ViewTypes
import com.ingjuanocampo.enfila.android.utils.navigateToCustomDest
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

// REVIEW CREATION PROCESS, CREATE CHAT TO CONTACT CLIENTS USING WHATS APP, LAUNCH APP
@AndroidEntryPoint
class FragmentListItems : Fragment() {
    private val navController by lazy { NavHostFragment.findNavController(this) }

    @Inject
    lateinit var navigationDestinations: NavigationDestinations

    private lateinit var binding: FragmentListItemsBinding

    companion object {
        fun newInstance() = FragmentListItems()
    }

    private lateinit var adapter: CompositeDelegateAdapter
    val viewModel: ViewModelListItems by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentListItemsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        val recycler: RecyclerView = view.findViewById(R.id.recycler)

        adapter =
            CompositeDelegateAdapter(1).apply {
                appendDelegate(
                    ViewTypes.SHIFT.ordinal,
                ) {
                    DelegateShift(it, ::stopListener, onShiftListener = {
                        navController.navigateToCustomDest(
                            navigationDestinations.navigateToShiftDetails(
                                it,
                            ),
                        )
                    })
                }
            }
        recycler.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                OrientationHelper.VERTICAL,
            ),
        )
        recycler.adapter = adapter
        viewModel.state.observe(
            viewLifecycleOwner,
        ) {
            if (it.isEmpty()) {
                binding.emptyComposeContainer.isVisible = true
                binding.swipe.isVisible = false
                binding.emptyComposeContainer.setContent {
                    GenericEmptyState(
                        title = getString(R.string.empty_fragment_list),
                        icon = Icons.Outlined.List,
                    )
                }
            } else {
                binding.swipe.isVisible = true
                binding.emptyComposeContainer.isVisible = false
                adapter.updateItems(it)
            }
        }
        viewModel.load(bundle = arguments)
    }

    fun stopListener(id: String) {
        viewModel.finish(id)
    }
}
