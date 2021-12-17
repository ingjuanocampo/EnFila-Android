package com.ingjuanocampo.enfila.android.lobby.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.ingjuanocampo.cdapter.DelegateViewHolder
import com.ingjuanocampo.cdapter.RecyclerViewType
import com.ingjuanocampo.enfila.android.databinding.DelegateShiftBinding
import com.ingjuanocampo.enfila.android.lobby.list.model.ShiftItem
import com.ingjuanocampo.enfila.android.utils.set

class DelegateShift(
    parent: ViewGroup,
    private val listener: (String) -> Unit = {},
    private val binding: DelegateShiftBinding =
        DelegateShiftBinding.inflate(LayoutInflater.from(parent.context), parent, false)
) :
    DelegateViewHolder(binding.root) {


    override fun onBindViewHolder(recyclerViewType: RecyclerViewType) {
        with(binding) {
            val shiftItem = recyclerViewType as ShiftItem
            currentTurn.text = shiftItem.currentTurn
            number.text = shiftItem.phone
            name.text = shiftItem.name
            state.text = shiftItem.state
            timeElapse.set(recyclerViewType)

            progressContainer.finish.isVisible = recyclerViewType.isCancellable
            progressContainer.set(listener, recyclerViewType.id)
        }


    }
}
