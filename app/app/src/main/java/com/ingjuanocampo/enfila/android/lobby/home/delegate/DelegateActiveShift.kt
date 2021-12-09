package com.ingjuanocampo.enfila.android.lobby.home.delegate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.ingjuanocampo.cdapter.DelegateViewHolder
import com.ingjuanocampo.cdapter.RecyclerViewType
import com.ingjuanocampo.enfila.android.databinding.DelegateActiveTurnBinding
import com.ingjuanocampo.enfila.android.lobby.list.model.ShiftItem

class DelegateActiveShift(val parent: ViewGroup,
                          private val biding : DelegateActiveTurnBinding = DelegateActiveTurnBinding.inflate(
                             LayoutInflater.from(parent.context), parent, false),
                          private val listener: (String) -> Unit): DelegateViewHolder(biding.root) {

    override fun onBindViewHolder(recyclerViewType: RecyclerViewType) {
        recyclerViewType as ShiftItem
        biding.currentNumber.text = recyclerViewType.currentTurn
        biding.clientPhone.text = recyclerViewType.phone
        biding.clientName.text = recyclerViewType.name
        setProgressVisible(false)
        biding.finish.setOnClickListener {
            setProgressVisible(true)
            listener.invoke(recyclerViewType.id)
        }
    }

    private fun setProgressVisible(isProgressVisible: Boolean) {
        biding.progress.isVisible = isProgressVisible
        biding.stopButton.isVisible = !isProgressVisible
    }
}