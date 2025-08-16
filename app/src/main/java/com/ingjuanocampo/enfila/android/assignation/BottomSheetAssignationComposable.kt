package com.ingjuanocampo.enfila.android.assignation

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ingjuanocampo.enfila.android.assignation.viewmodel.ViewModelAssignation
import com.ingjuanocampo.enfila.android.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomSheetAssignationComposable : BottomSheetDialogFragment() {
    private val viewModel: ViewModelAssignation by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                AppTheme {
                    AssignationComposable(
                        viewModel = viewModel,
                        onClose = { dismiss() },
                    )
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Make the bottom sheet full screen for better UX
        setStyle(STYLE_NORMAL, com.google.android.material.R.style.Theme_Material3_DayNight_BottomSheetDialog)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        // Configure window for proper keyboard handling
        dialog.window?.let { window ->
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            // Alternative: SOFT_INPUT_ADJUST_PAN if resize doesn't work well
            // window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        }

        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            val bottomSheet =
                bottomSheetDialog.findViewById<View>(
                    com.google.android.material.R.id.design_bottom_sheet,
                )

            bottomSheet?.let {
                val behavior = BottomSheetBehavior.from(it)
                // Set the bottom sheet to be fully expanded
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                // Disable dragging to collapse (optional - remove if you want users to be able to collapse)
                behavior.isDraggable = true
                // Set peek height to full height
                behavior.peekHeight = BottomSheetBehavior.PEEK_HEIGHT_AUTO
                // Skip the collapsed state
                behavior.skipCollapsed = true
                // Ensure the bottom sheet can handle keyboard properly
                behavior.isFitToContents = false
                behavior.halfExpandedRatio = 0.6f
            }
        }

        return dialog
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        // Additional setup to ensure the bottom sheet is fully expanded
        dialog?.let { dialog ->
            val bottomSheetDialog = dialog as BottomSheetDialog
            val bottomSheet =
                bottomSheetDialog.findViewById<View>(
                    com.google.android.material.R.id.design_bottom_sheet,
                )

            bottomSheet?.let {
                val behavior = BottomSheetBehavior.from(it)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }
}
