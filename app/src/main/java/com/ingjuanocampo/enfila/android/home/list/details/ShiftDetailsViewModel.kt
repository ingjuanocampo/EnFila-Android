package com.ingjuanocampo.enfila.android.home.list.details

import android.os.Bundle
import androidx.lifecycle.viewModelScope
import com.ingjuanocampo.common.composable.MviBaseViewModel
import com.ingjuanocampo.enfila.android.home.list.model.ShiftItem
import com.ingjuanocampo.enfila.android.home.list.model.mapToUI
import com.ingjuanocampo.enfila.android.utils.launchGeneral
import com.ingjuanocampo.enfila.domain.usecases.FinishShiftUC
import com.ingjuanocampo.enfila.domain.usecases.HomeUC
import com.ingjuanocampo.enfila.domain.usecases.LoadShiftDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class ShiftDetailsViewModel
@Inject
constructor(
    private val loadShiftDetails: LoadShiftDetails,
    private val homeUC: HomeUC,
    private val finishShiftUC: FinishShiftUC
) : MviBaseViewModel<ShiftItem>(ShiftItem()) {
    private var id: String? = null

    fun init(arguments: Bundle) {
        launchGeneral {
            id = arguments.getString("id")
            val shiftClient = loadShiftDetails.invoke(id!!).mapToUI()
            _state.value = shiftClient
        }
    }

    fun onCancel() {
        // TODO place a loader, make sure to reload or observe, and fix UI detaisl
        viewModelScope.launchGeneral {
            setProgress()
            homeUC.cancel(id!!).reLoad()
        }
    }

    private suspend fun Flow<*>.reLoad() =
        this.map {
            loadShiftDetails.invoke(id!!).mapToUI()
        }.collect {
            _state.value = it
        }

    private fun setProgress() {
        setState {
            copy(isProcessingActions = true)
        }
    }

    fun onActive() {
        viewModelScope.launchGeneral {
            setProgress()
            homeUC.next(id!!).reLoad()
        }
    }

    fun onFinish() {
        viewModelScope.launchGeneral {
            setProgress()
            finishShiftUC.invoke(id!!).reLoad()
        }
    }
}
