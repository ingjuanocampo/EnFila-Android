package com.ingjuanocampo.enfila.android.home.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import com.ingjuanocampo.cdapter.CompositeDelegateAdapter
import com.ingjuanocampo.enfila.android.R
import com.ingjuanocampo.enfila.android.home.list.adapter.DelegateShift
import com.ingjuanocampo.enfila.android.home.list.viewmodel.ViewModelListItems
import com.ingjuanocampo.enfila.android.utils.ViewTypes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentHistory : Fragment() {

    private val navController by lazy { findNavController(this) }
    companion object {
        fun newInstance() = FragmentHistory()
    }

    private lateinit var adapter: CompositeDelegateAdapter
    val viewModel: ViewModelListItems by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_list_items, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recycler: RecyclerView = view.findViewById(R.id.recycler)

        adapter = CompositeDelegateAdapter(1).apply {
            appendDelegate(
                ViewTypes.SHIFT.ordinal,
            ) { DelegateShift(it, onShiftListener = {
                val bundle = bundleOf("id" to it)
                navController.navigate(R.id.action_fragmentShiftPager_to_fragmentShiftDetail, bundle)
            }) }
        }
        recycler.addItemDecoration(DividerItemDecoration(requireContext(), OrientationHelper.VERTICAL))
        recycler.adapter = adapter
        viewModel.state.observe(
            viewLifecycleOwner,
            Observer {
                adapter.updateItems(it)
            },
        )

        viewModel.load(false)
    }
}
