package com.ingjuanocampo.enfila.android.login.new_account.viewmodel

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ingjuanocampo.enfila.android.utils.launchGeneral
import com.ingjuanocampo.enfila.domain.entity.User
import com.ingjuanocampo.enfila.domain.usecases.signing.SignInUC
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ViewModelFragmentProfile
    @Inject
    constructor(
        private val signUc: SignInUC,
    ) : ViewModel() {
        private var id: String? = null
        private var phone: String? = null
        val state = MutableLiveData<ProfileState>()

        fun init(arguments: Bundle?) {
            launchGeneral {
                phone = arguments?.getString("phone")
                id = arguments?.getString("id")

                if (phone != null) {
                    state.postValue(ProfileState.CreationFlow(phone!!))
                } else {
                    // Load info internally
                }
            }
        }

    fun createUserAndLogin(
        name: String,
        companyName: String,
    ) {
        launchGeneral {
            // Create user without ID - backend will use phone as ID
            val userToCreate = User(
                id = "", // Empty ID - backend will use phone
                phone = phone!!,
                name = name
            )
            
            val result = signUc.createUserAndSignIn(userToCreate, companyName)
            state.postValue(ProfileState.AuthProcess(result))
        }
    }
}
