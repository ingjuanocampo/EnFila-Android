package com.ingjuanocampo.enfila.android.lobby.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import com.ingjuanocampo.cdapter.CompositeDelegateAdapter
import com.ingjuanocampo.enfila.android.R
import com.ingjuanocampo.enfila.android.lobby.list.adapter.DelegateShift
import com.ingjuanocampo.enfila.android.lobby.list.viewmodel.ViewModelListItems
import com.ingjuanocampo.enfila.android.utils.ViewTypes

class FragmentHistory : Fragment() {

    companion object {
        fun newInstance() = FragmentHistory()
    }

    private lateinit var adapter: CompositeDelegateAdapter
    val viewModel: ViewModelListItems by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list_items, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recycler: RecyclerView = view.findViewById(R.id.recycler)

        adapter = CompositeDelegateAdapter(1).apply {
            appendDelegate(
                ViewTypes.SHIFT.ordinal
            ) { DelegateShift(it) }
        }
        recycler.addItemDecoration(DividerItemDecoration(requireContext(), OrientationHelper.VERTICAL))
        recycler.adapter = adapter
        viewModel.state.observe(viewLifecycleOwner, Observer {
            adapter.addNewItems(it)
        })

        viewModel.load(false)

    }

}