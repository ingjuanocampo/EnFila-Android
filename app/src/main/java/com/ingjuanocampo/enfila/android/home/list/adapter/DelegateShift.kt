package com.ingjuanocampo.enfila.android.home.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.ingjuanocampo.cdapter.DelegateViewHolder
import com.ingjuanocampo.cdapter.RecyclerViewType
import com.ingjuanocampo.enfila.android.databinding.DelegateShiftBinding
import com.ingjuanocampo.enfila.android.home.list.model.ShiftItem
import com.ingjuanocampo.enfila.android.utils.set
import com.ingjuanocampo.enfila.domain.entity.ShiftState

class DelegateShift(
    parent: ViewGroup,
    private val listener: (String) -> Unit = {},
    private val binding: DelegateShiftBinding =
        DelegateShiftBinding.inflate(LayoutInflater.from(parent.context), parent, false),
    private val onShiftListener: (String) -> Unit = {},
) :
    DelegateViewHolder(binding.root) {
    override fun onBindViewHolder(recyclerViewType: RecyclerViewType) {
        with(binding) {
            val shiftItem = recyclerViewType as ShiftItem
            binding.root.setOnClickListener { onShiftListener(shiftItem.id) }
            currentTurn.text = shiftItem.currentTurn
            number.text = shiftItem.phone
            name.text = shiftItem.name
            state.text = shiftItem.state
            timeElapse.set(recyclerViewType)
            timeElapseContainer.isVisible = recyclerViewType.state == ShiftState.WAITING.name
            issueDate.text = shiftItem.formmattedIssueDate

            progressContainer.finish.isVisible = recyclerViewType.isCancellable
            progressContainer.set(listener, recyclerViewType.id)
        }
    }
}
