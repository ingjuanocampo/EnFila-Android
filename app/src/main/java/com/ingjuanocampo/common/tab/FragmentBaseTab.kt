package com.ingjuanocampo.common.tab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.ingjuanocampo.enfila.android.databinding.FragmentBaseTabBinding

abstract class FragmentBaseTab : Fragment() {
    protected lateinit var binding: FragmentBaseTabBinding

    abstract val listItems: List<FragmentTabItem>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentBaseTabBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter =
            object : FragmentStateAdapter(requireActivity()) {
                override fun getItemCount(): Int {
                    return listItems.size
                }

                override fun createFragment(position: Int): Fragment {
                    return listItems[position].fragmentInstance()
                }
            }

        TabLayoutMediator(binding.toolbarTabs, binding.viewPager) { tab, pos ->
            tab.text = listItems[pos].title
        }.attach()
    }
}
