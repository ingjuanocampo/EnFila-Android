package com.ingjuanocampo.enfila.android.home.home

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddHome
import androidx.compose.material.icons.outlined.AddHome
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ingjuanocampo.cdapter.CompositeDelegateAdapter
import com.ingjuanocampo.common.composable.GenericEmptyState
import com.ingjuanocampo.enfila.android.R
import com.ingjuanocampo.enfila.android.assignation.ActivityAssignation
import com.ingjuanocampo.enfila.android.databinding.FragmentHomeBinding
import com.ingjuanocampo.enfila.android.home.home.delegate.*
import com.ingjuanocampo.enfila.android.home.home.viewmodel.ViewModelHome
import com.ingjuanocampo.enfila.android.utils.ViewTypes
import com.ingjuanocampo.enfila.domain.state.home.HomeState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentHome : Fragment() {
    companion object {
        fun newInstance() = FragmentHome()
    }

    private var toolbar: Toolbar? = null

    private val viewModel: ViewModelHome by viewModels()

    private lateinit var binding: FragmentHomeBinding

    private val navController by lazy { findNavController() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = CompositeDelegateAdapter(10)

        adapter.appendDelegate(ViewTypes.HOME_RESUME.ordinal) { DelegateResume(it) }
        adapter.appendDelegate(ViewTypes.ACTIVE_SHIFT.ordinal) {
            DelegateActiveShift(it, finishListener = { id ->
                viewModel.finish(id)
            }, onShiftListener = ::navigateToDetails)
        }

        adapter.appendDelegate(ViewTypes.EMPTY_VIEW.ordinal) {
            DelegateEmpty(it)
        }

        adapter.appendDelegate(ViewTypes.NEXT_SHIFT.ordinal) {
            DelegateNextShift(it, listener = { actions ->
                when (actions) {
                    is NextShiftActions.Active -> viewModel.next(actions.id)
                    is NextShiftActions.Cancel -> viewModel.cancel(actions.id)
                }
            }, onShiftSelected = ::navigateToDetails)
        }

        adapter.appendDelegate(ViewTypes.HEADER_LINK.ordinal) { DelegateHeaderLink(it) }

        binding.rvHome.adapter = adapter

        toolbar = view.findViewById(R.id.toolbarWidget)
        setHasOptionsMenu(true)
        viewModel.loadCurrentTurn()
        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                HomeState.Loading -> {
                    binding.emptyComposeContainer.isVisible = false
                }
                HomeState.Empty -> {
                    binding.emptyComposeContainer.isVisible = true
                    binding.emptyComposeContainer.setContent {
                        GenericEmptyState(
                            title = getString(R.string.empty_home),
                            icon = Icons.Outlined.AddHome,
                        )
                    }
                }
                is HomeState.HomeLoaded -> {
                    binding.emptyComposeContainer.isVisible = false
                    adapter.updateItems(it.items)
                }
            }
        }

        binding.addButton.setOnClickListener {
            startAdditionProcess()
        }
    }

    private fun navigateToDetails(it: String) {
        val bundle = bundleOf("id" to it)
        navController.navigate(R.id.action_fragmentHome_to_navigation, bundle)
    }

    private fun startAdditionProcess() {
/*        BottomSheetAssignation().apply {
            // setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_Dialog_Transparent_Adjust_Resize)
        }.show(requireActivity().supportFragmentManager, "")*/

        startActivity(Intent(requireContext(), ActivityAssignation::class.java))
    }

    override fun onCreateOptionsMenu(
        menu: Menu,
        inflater: MenuInflater,
    ) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.dashboard_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        startAdditionProcess()
        return true
    }
}
