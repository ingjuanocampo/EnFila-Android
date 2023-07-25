package com.ingjuanocampo.enfila.android.assignation

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ingjuanocampo.enfila.android.R
import com.ingjuanocampo.enfila.android.assignation.viewmodel.AssignationState
import com.ingjuanocampo.enfila.android.assignation.viewmodel.ViewModelAssignation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomSheetAssignation : BottomSheetDialogFragment() {

    private val navController by lazy { findNavController(this) }
    private val viewModel: ViewModelAssignation by viewModels(ownerProducer = { this })

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)


        return dialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.activity_assignation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.assignationState.observe(
            viewLifecycleOwner,
            Observer {
                if (it is AssignationState.AssignationSet) {
                    dismiss()
                }
            },
        )
    }
}
