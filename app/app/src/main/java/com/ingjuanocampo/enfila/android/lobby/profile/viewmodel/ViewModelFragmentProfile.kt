package com.ingjuanocampo.enfila.android.lobby.profile.viewmodel

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ingjuanocampo.enfila.android.utils.launchGeneral
import com.ingjuanocampo.enfila.di.AppComponent
import com.ingjuanocampo.enfila.domain.entity.User
import kotlinx.coroutines.flow.collect

class ViewModelFragmentProfile : ViewModel() {

    private var id: String? = null
    private var phone: String? = null
    val state = MutableLiveData<ProfileState>()
    val signUC = AppComponent.provideSignUC()

    fun init(arguments: Bundle?) {
        launchGeneral {
            phone = arguments?.getString("phone")
            id = arguments?.getString("id")

            if (phone != null ) {
                state.postValue(ProfileState.CreationFlow(phone!!))
            } else {
                // Load info internally
            }
        }
    }

    fun createUserAndLogin(name: String, companyName: String) {
        launchGeneral {
            signUC.createUserAndSignIn(User(id = id!!, phone = phone!!, name = name),companyName).collect {
                state.postValue(ProfileState.AuthProcess(it))
            }
        }
    }


}