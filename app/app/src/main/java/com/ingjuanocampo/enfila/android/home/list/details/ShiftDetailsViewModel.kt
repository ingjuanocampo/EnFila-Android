package com.ingjuanocampo.enfila.android.home.list.details

import android.os.Bundle
import com.ingjuanocampo.common.composable.MviBaseViewModel
import com.ingjuanocampo.enfila.android.home.list.model.ShiftItem
import com.ingjuanocampo.enfila.android.home.list.model.mapToUI
import com.ingjuanocampo.enfila.android.utils.launchGeneral
import com.ingjuanocampo.enfila.domain.usecases.LoadShiftDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShiftDetailsViewModel @Inject constructor(
    private val loadShiftDetails: LoadShiftDetails
): MviBaseViewModel<ShiftItem>(ShiftItem()) {

    fun init(arguments: Bundle) {
        launchGeneral {
           val id =  arguments.getString("id")
            val shiftClient = loadShiftDetails.invoke(id!!).mapToUI()
           _state.value  = shiftClient
        }
    }
}