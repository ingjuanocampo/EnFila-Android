package com.ingjuanocampo.enfila.android.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ingjuanocampo.enfila.android.utils.launchGeneral
import com.ingjuanocampo.enfila.domain.usecases.LoadInitialInfoUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class ViewModelSplash
@Inject
constructor(
    val loadInitInfoUC: LoadInitialInfoUC
) : ViewModel() {
    val state = MutableLiveData<SplashState>()

    fun launchSplash() {
        launchGeneral {
            loadInitInfoUC().let {
                delay(TimeUnit.SECONDS.toMillis(1))
                state.postValue(Navigate)
            }
        }
    }
}
