package com.ingjuanocampo.enfila.android.assignation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.ingjuanocampo.enfila.android.R
import com.ingjuanocampo.enfila.android.assignation.viewmodel.ViewModelAssignation

class FragmentTurn : Fragment() {
    private val navController by lazy { NavHostFragment.findNavController(this) }

    private val viewModel: ViewModelAssignation by viewModels(ownerProducer = { requireParentFragment().requireParentFragment() })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_turn, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val back = view.findViewById<ImageView>(R.id.back)
        val nameTv = view.findViewById<TextView>(R.id.nameTv).apply {
            text = viewModel.name
        }
        val phoneTv = view.findViewById<TextView>(R.id.phoneTv).apply {
            text = viewModel.phoneNumber
        }
        val notesTv = view.findViewById<TextView>(R.id.notesTv)
            .apply {
                text = viewModel.note
            }
        val turnEd = view.findViewById<TextView>(R.id.turnEd).apply {
            setText("${viewModel.closestTurn}")
        }

        turnEd.addTextChangedListener { editable ->
            viewModel.setTurn(editable.toString().toInt())
        }

        back.setOnClickListener {
            navController.popBackStack()
        }

        val next = view.findViewById<Button>(R.id.next)
        next.setOnClickListener {
            viewModel.createAssignation()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentTurn()
    }
}