package com.ingjuanocampo.enfila.android.home.home.delegate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.ingjuanocampo.cdapter.DelegateViewHolder
import com.ingjuanocampo.cdapter.RecyclerViewType
import com.ingjuanocampo.enfila.android.databinding.DelegateNextTurnBinding
import com.ingjuanocampo.enfila.android.home.list.model.ShiftItem
import com.ingjuanocampo.enfila.android.utils.set

class DelegateNextShift(
    val parent: ViewGroup,
    private val biding: DelegateNextTurnBinding =
        DelegateNextTurnBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        ),
    val listener: (NextShiftActions) -> Unit,
    val onShiftSelected: (String) -> Unit,
) : DelegateViewHolder(biding.root) {
    override fun onBindViewHolder(recyclerViewType: RecyclerViewType) {
        recyclerViewType as ShiftItem
        biding.waitTime.set(recyclerViewType)
        biding.root.setOnClickListener {
            onShiftSelected.invoke(recyclerViewType.id)
        }
        biding.currentNumber.text = recyclerViewType.currentTurn
        biding.clientPhone.text = recyclerViewType.phone
        biding.clientName.text = recyclerViewType.name
        setButtonsVisible(true)
        biding.activeTurn.setOnClickListener {
            setButtonsVisible(false)
            listener.invoke(NextShiftActions.Active(recyclerViewType.id))
        }

        biding.cancelTurn.setOnClickListener {
            setButtonsVisible(false)
            listener.invoke(NextShiftActions.Cancel(recyclerViewType.id))
        }
    }

    private fun setButtonsVisible(visible: Boolean) {
        biding.progressBar.isVisible = !visible
        biding.buttonsContainer.isVisible = visible
    }
}

sealed class NextShiftActions {
    data class Cancel(val id: String) : NextShiftActions()

    data class Active(val id: String) : NextShiftActions()
}
