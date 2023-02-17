package com.ingjuanocampo.enfila.android.assignation

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
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

        dialog.setOnShowListener { dialog ->
            val d = dialog as BottomSheetDialog
            val bottomSheet =
                d.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout?
            BottomSheetBehavior.from<FrameLayout?>(bottomSheet!!).state =
                BottomSheetBehavior.STATE_EXPANDED
        }

        return dialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_assignation, container, true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.assignationState.observe(viewLifecycleOwner, Observer {
            if (it is AssignationState.AssignationSet) {
                dismiss()
            }
        })

    }


}