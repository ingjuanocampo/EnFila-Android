package com.ingjuanocampo.enfila.android.assignation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ingjuanocampo.enfila.android.R
import com.ingjuanocampo.enfila.android.assignation.viewmodel.ViewModelAssignation
import dagger.hilt.android.AndroidEntryPoint

// TODO navigation and general error handling
@AndroidEntryPoint
class FragmentNameNote : Fragment() {
    private val navController by lazy { findNavController() }

    private val viewModel: ViewModelAssignation by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_name_note, container, false)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        val nameEd =
            view.findViewById<EditText>(R.id.nameEd).apply {
                setText(viewModel.name)
            }
        val noteEd =
            view.findViewById<EditText>(R.id.noteEd).apply {
                setText(viewModel.note)
            }

        nameEd.addTextChangedListener { editable ->
            viewModel.name = editable.toString()
        }
        noteEd.addTextChangedListener { editable ->
            viewModel.note = editable.toString()
        }
        val next = view.findViewById<Button>(R.id.nextCta)
        next.setOnClickListener {
            navController.navigate(R.id.action_fragmentNameAndNote_to_fragmentTurn)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentNameNote()
    }
}
