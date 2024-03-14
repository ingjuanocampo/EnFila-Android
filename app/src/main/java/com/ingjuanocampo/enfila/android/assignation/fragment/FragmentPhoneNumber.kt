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
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import com.ingjuanocampo.enfila.android.R
import com.ingjuanocampo.enfila.android.assignation.viewmodel.AssignationState
import com.ingjuanocampo.enfila.android.assignation.viewmodel.ViewModelAssignation

class FragmentPhoneNumber : Fragment() {
    private val navController by lazy { NavHostFragment.findNavController(this) }

    companion object {
        fun newInstance() = FragmentPhoneNumber()
    }

    private val viewModel: ViewModelAssignation by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_phone_number, container, false)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        val next = view.findViewById<Button>(R.id.nextCta)
        next.setOnClickListener {
            navController.navigate(R.id.action_fragmentNumber_to_fragmentNameAndNote)
        }
        next.isEnabled = false

        val phoneNumber = view.findViewById<EditText>(R.id.phoneNumber)
        phoneNumber.setText(viewModel.phoneNumber)

        phoneNumber.addTextChangedListener {
            viewModel.phoneNumber = (it.toString())
        }

        viewModel.assignationState.observe(
            viewLifecycleOwner,
            Observer {
                if (it is AssignationState.NumberSet) {
                    next.isEnabled = true
                }
            },
        )
    }
}
