package com.ingjuanocampo.enfila.android.home.profile.viewmodel

import androidx.lifecycle.ViewModel
import com.ingjuanocampo.enfila.android.home.profile.ProfileView
import com.ingjuanocampo.enfila.android.home.profile.domain.ProfileCard
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProfileViewModel: ViewModel() {

    val state = MutableStateFlow(
        ProfileCard(
            "User",
            "31311231312",
            "Company",
            "112",
            "100"
        ))

}