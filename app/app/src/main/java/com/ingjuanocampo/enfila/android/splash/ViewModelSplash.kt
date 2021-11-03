package com.ingjuanocampo.enfila.android.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ingjuanocampo.enfila.android.utils.launchGeneral
import com.ingjuanocampo.enfila.di.AppComponent
import com.ingjuanocampo.enfila.domain.usecases.LoadInitialInfoUC
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import java.util.concurrent.TimeUnit

class ViewModelSplash : ViewModel() {

    val state = MutableLiveData<SplashState>()

    val loadInitInfoUC : LoadInitialInfoUC = AppComponent.domainModule.provideLoadInitialInfo()

    fun launchSplash() {
        launchGeneral {
            loadInitInfoUC().let {
                delay(TimeUnit.SECONDS.toMillis(1))
                state.postValue(Navigate)
            }
        }
    }

}