package com.ingjuanocampo.enfila.android.lobby.home

import android.os.Bundle
import android.os.SystemClock
import android.view.*
import android.widget.Chronometer
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ingjuanocampo.cdapter.CompositeDelegateAdapter
import com.ingjuanocampo.enfila.android.R
import com.ingjuanocampo.enfila.android.assignation.BottomSheetAssignation
import com.ingjuanocampo.enfila.android.databinding.FragmentHomeBinding
import com.ingjuanocampo.enfila.android.lobby.home.delegate.DelegateActiveShift
import com.ingjuanocampo.enfila.android.lobby.home.delegate.DelegateHeaderLink
import com.ingjuanocampo.enfila.android.lobby.home.delegate.DelegateNextShift
import com.ingjuanocampo.enfila.android.lobby.home.delegate.DelegateResume
import com.ingjuanocampo.enfila.domain.state.home.HomeState
import com.ingjuanocampo.enfila.android.lobby.home.viewmodel.ViewModelHome
import com.ingjuanocampo.enfila.android.utils.ViewTypes


class FragmentHome : Fragment() {

    companion object {
        fun newInstance() = FragmentHome()
    }

    private var toolbar: Toolbar? = null

    private val viewModel: ViewModelHome by viewModels()

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater,  container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*view.findViewById<View>(R.id.callAction).setOnClickListener {
            viewModel.next()
        }*/

        val adapter = CompositeDelegateAdapter(10)

        adapter.appendDelegate(ViewTypes.HOME_RESUME.ordinal) { DelegateResume(it) }
        adapter.appendDelegate(ViewTypes.ACTIVE_SHIFT.ordinal) { DelegateActiveShift(it) }
        adapter.appendDelegate(ViewTypes.NEXT_SHIFT.ordinal) { DelegateNextShift(it) }
        adapter.appendDelegate(ViewTypes.HEADER_LINK.ordinal) { DelegateHeaderLink(it) }

        binding.rvHome.adapter = adapter

        toolbar = view.findViewById(R.id.toolbarWidget)
        setHasOptionsMenu(true)
        viewModel.loadCurrentTurn()
        viewModel.state.observe(viewLifecycleOwner, {
            when (it) {
                HomeState.Loading -> {

                }
                HomeState.Empty -> {

                }
                is HomeState.HomeLoaded -> {
                    adapter.updateItems(it.items)
                }
            }
        })

        binding.toolbar.addButton.setOnClickListener {
            BottomSheetAssignation().apply {
            }.show(requireActivity().supportFragmentManager, "")
        }


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.dashboard_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        BottomSheetAssignation().apply {
        }.show(requireActivity().supportFragmentManager, "")
        return true
    }

    /*
    private fun setEmpty() {
        currentNumber?.text = "---"
        clientName?.text = "---"
        clientPhone?.text = "---"
        waitTime?.text = "---"
    }

    private fun updateShift(shift: ShiftWithClient?) {
        shift?.let {
            clientName?.text = shift.client.name
            clientPhone?.text = shift.client.id
            waitTime?.text = "${shift.shift.date}"
            waitTime?.base =
                SystemClock.elapsedRealtime() - TimeUnit.SECONDS.toMillis(shift.shift.getDiffTime())
            waitTime?.start()
            currentNumber?.text = "${shift.shift.number}"
        } ?: setEmpty()
    }

     */


}