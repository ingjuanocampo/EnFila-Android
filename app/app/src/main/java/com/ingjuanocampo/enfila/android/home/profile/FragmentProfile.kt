package com.ingjuanocampo.enfila.android.home.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.ingjuanocampo.enfila.android.home.profile.domain.ProfileCard

class FragmentProfile: Fragment() {


    companion object {
        fun newInstance() = FragmentProfile()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                ProfileView(
                    ProfileCard(
                    "User",
                    "31311231312",
                    "Company",
                    "112",
                    "100"
                )
                )
            }
        }
    }
}