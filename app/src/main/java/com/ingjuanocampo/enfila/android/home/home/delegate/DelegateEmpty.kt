package com.ingjuanocampo.enfila.android.home.home.delegate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddHome
import com.ingjuanocampo.cdapter.DelegateViewHolder
import com.ingjuanocampo.cdapter.RecyclerViewType
import com.ingjuanocampo.common.composable.GenericEmptyState
import com.ingjuanocampo.enfila.android.databinding.ComposableItemBinding
import com.ingjuanocampo.enfila.android.home.list.model.EmptyDelegate

class DelegateEmpty(
    val parent: ViewGroup,
    private val binding: ComposableItemBinding =
        ComposableItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
) : DelegateViewHolder(binding.root) {
    override fun onBindViewHolder(recyclerViewType: RecyclerViewType) {
        recyclerViewType as EmptyDelegate
        binding.emptyComposeContainer.setContent {
            GenericEmptyState(title = recyclerViewType.title, icon = Icons.Outlined.AddHome)
        }
    }
}
