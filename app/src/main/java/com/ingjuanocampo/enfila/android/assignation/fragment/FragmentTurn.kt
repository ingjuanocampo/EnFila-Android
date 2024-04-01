package com.ingjuanocampo.enfila.android.assignation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import com.ingjuanocampo.enfila.android.R
import com.ingjuanocampo.enfila.android.assignation.viewmodel.AssignationState
import com.ingjuanocampo.enfila.android.assignation.viewmodel.ViewModelAssignation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentTurn : Fragment() {
    private val navController by lazy { NavHostFragment.findNavController(this) }

    private val viewModel: ViewModelAssignation by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_turn, container, false)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        val nameTv =
            view.findViewById<TextView>(R.id.nameTv).apply {
                text = viewModel.name
            }
        val phoneTv =
            view.findViewById<TextView>(R.id.phoneTv).apply {
                text = viewModel.phoneNumber
            }
        val notesTv =
            view.findViewById<TextView>(R.id.notesTv)
                .apply {
                    text = viewModel.note
                }
        view.findViewById<TextView>(R.id.turnEd).apply {
            setText("${viewModel.closestTurn}")
        }

        val next = view.findViewById<Button>(R.id.next)
        val progressBar = view.findViewById<ProgressBar>(R.id.progress)

        next.setOnClickListener {
            viewModel.createAssignation()
        }

        viewModel.assignationState.observe(viewLifecycleOwner) {
            when (it) {
                AssignationState.Loading -> {
                    progressBar.isVisible = true
                    next.isVisible = false
                }
                AssignationState.AssignationSet -> requireActivity().finish()
                else -> {}
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(
            param1: String,
            param2: String,
        ) = FragmentTurn()
    }
}
