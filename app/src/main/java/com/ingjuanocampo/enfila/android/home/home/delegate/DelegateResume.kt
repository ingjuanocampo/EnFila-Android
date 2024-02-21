package com.ingjuanocampo.enfila.android.home.home.delegate

import android.view.LayoutInflater
import android.view.ViewGroup
import com.ingjuanocampo.cdapter.DelegateViewHolder
import com.ingjuanocampo.cdapter.RecyclerViewType
import com.ingjuanocampo.enfila.android.databinding.DelegateShiftsInfoBinding
import com.ingjuanocampo.enfila.domain.usecases.model.HomeResume

class DelegateResume(
    val parent: ViewGroup,
    private val biding: DelegateShiftsInfoBinding = DelegateShiftsInfoBinding.inflate(
        LayoutInflater.from(parent.context),
        parent,
        false,
        ),
) : DelegateViewHolder(biding.root) {

    override fun onBindViewHolder(recyclerViewType: RecyclerViewType) {
        recyclerViewType as HomeResume
        biding.totalAverageTime.text = recyclerViewType.avrTime
        biding.totalInline.text = "" + recyclerViewType.totalTurns
    }
}
