package com.ingjuanocampo.enfila.android.home.home.delegate

import android.view.LayoutInflater
import android.view.ViewGroup
import com.ingjuanocampo.cdapter.DelegateViewHolder
import com.ingjuanocampo.cdapter.RecyclerViewType
import com.ingjuanocampo.enfila.android.databinding.DelegateHeaderLinkBinding
import com.ingjuanocampo.enfila.android.home.list.model.HeaderItem

class DelegateHeaderLink(
    val parent: ViewGroup,
    private val biding: DelegateHeaderLinkBinding =
        DelegateHeaderLinkBinding.inflate(
            LayoutInflater.from(
                parent.context
            ),
            parent,
            false
        )
) : DelegateViewHolder(biding.root) {
    override fun onBindViewHolder(recyclerViewType: RecyclerViewType) {
        recyclerViewType as HeaderItem

        biding.homeTitle.text = recyclerViewType.headerText

        recyclerViewType.linkText?.let {
            biding.headerLinkText.text = recyclerViewType.linkText
        }

        recyclerViewType.icon?.let {
            biding.headerIcon.setImageResource(recyclerViewType.icon)
        }
    }
}
