package com.ingjuanocampo.enfila.android.assignation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.ingjuanocampo.enfila.android.R
import com.ingjuanocampo.enfila.android.assignation.viewmodel.ViewModelAssignation


class FragmentNameNote : Fragment() {

    private val navController by lazy { NavHostFragment.findNavController(this) }

    private val viewModel: ViewModelAssignation by viewModels(ownerProducer = { requireParentFragment().requireParentFragment() })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_name_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val back = view.findViewById<ImageView>(R.id.back)
        val nameEd = view.findViewById<EditText>(R.id.nameEd).apply {
            setText(viewModel.name)
        }
        val noteEd = view.findViewById<EditText>(R.id.noteEd).apply {
            setText(viewModel.note)
        }

        nameEd.addTextChangedListener { editable ->
            viewModel.name = editable.toString()
        }
        noteEd.addTextChangedListener { editable ->
        viewModel.note = editable.toString()
        }
        back.setOnClickListener {
            navController.popBackStack()
        }
        val next = view.findViewById<ImageView>(R.id.next)
        next.setOnClickListener {
            navController.navigate(R.id.action_fragmentNameAndNote_to_fragmentTurn)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            FragmentNameNote()
    }
}