package com.ingjuanocampo.enfila.android.home.home.delegate

import android.view.LayoutInflater
import android.view.ViewGroup
import com.ingjuanocampo.cdapter.DelegateViewHolder
import com.ingjuanocampo.cdapter.RecyclerViewType
import com.ingjuanocampo.enfila.android.databinding.DelegateActiveTurnBinding
import com.ingjuanocampo.enfila.android.home.list.model.ShiftItem
import com.ingjuanocampo.enfila.android.utils.set

class DelegateActiveShift(
    val parent: ViewGroup,
    private val biding: DelegateActiveTurnBinding = DelegateActiveTurnBinding.inflate(
        LayoutInflater.from(parent.context),
        parent,
        false,
    ),
    private val finishListener: (String) -> Unit,
    private val onShiftListener: (String) -> Unit,

    ) : DelegateViewHolder(biding.root) {

    override fun onBindViewHolder(recyclerViewType: RecyclerViewType) {
        recyclerViewType as ShiftItem
        biding.currentNumber.text = recyclerViewType.currentTurn
        biding.clientPhone.text = recyclerViewType.phone
        biding.clientName.text = recyclerViewType.name
        biding.root.setOnClickListener { onShiftListener.invoke(recyclerViewType.id) }
        biding.progressContainer.set(finishListener, recyclerViewType.id)
    }
}
