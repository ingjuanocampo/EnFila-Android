package com.ingjuanocampo.enfila.android.home.home

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ingjuanocampo.cdapter.CompositeDelegateAdapter
import com.ingjuanocampo.enfila.android.R
import com.ingjuanocampo.enfila.android.assignation.BottomSheetAssignation
import com.ingjuanocampo.enfila.android.databinding.FragmentHomeBinding
import com.ingjuanocampo.enfila.android.home.home.delegate.*
import com.ingjuanocampo.enfila.android.home.home.viewmodel.ViewModelHome
import com.ingjuanocampo.enfila.android.utils.ViewTypes
import com.ingjuanocampo.enfila.domain.state.home.HomeState


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
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val adapter = CompositeDelegateAdapter(10)

        adapter.appendDelegate(ViewTypes.HOME_RESUME.ordinal) { DelegateResume(it) }
        adapter.appendDelegate(ViewTypes.ACTIVE_SHIFT.ordinal) {
            DelegateActiveShift(it) { id ->
                viewModel.finish(id)
            }
        }

        adapter.appendDelegate(ViewTypes.NEXT_SHIFT.ordinal) {
            DelegateNextShift(it) { actions ->
                when (actions) {
                    is NextShiftActions.Active -> viewModel.next(actions.id)
                    is NextShiftActions.Cancel -> viewModel.cancel(actions.id)
                }
            }
        }

        adapter.appendDelegate(ViewTypes.HEADER_LINK.ordinal) { DelegateHeaderLink(it) }

        binding.rvHome.adapter = adapter

        toolbar = view.findViewById(R.id.toolbarWidget)
        setHasOptionsMenu(true)
        viewModel.loadCurrentTurn()
        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                HomeState.Loading -> {

                }
                HomeState.Empty -> {

                }
                is HomeState.HomeLoaded -> {
                    adapter.updateItems(it.items)
                }
            }
        }

        binding.addButton.setOnClickListener {
          startAdditionProcess()
        }


    }

    private fun startAdditionProcess() {
        BottomSheetAssignation().apply {
            //setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_Dialog_Transparent_Adjust_Resize)
        }.show(requireActivity().supportFragmentManager, "")

       // startActivity(Intent(requireContext(), ActivityAssignation::class.java))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.dashboard_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
       startAdditionProcess()
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