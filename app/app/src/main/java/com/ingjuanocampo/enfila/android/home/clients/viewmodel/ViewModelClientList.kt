package com.ingjuanocampo.enfila.android.home.clients.viewmodel

import com.ingjuanocampo.common.composable.MviBaseViewModel
import com.ingjuanocampo.enfila.android.utils.launchGeneral
import com.ingjuanocampo.enfila.domain.entity.Client
import com.ingjuanocampo.enfila.domain.usecases.LoadClientListInformation
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class ViewModelClientList @Inject constructor(
    private val loadClientListInformation: LoadClientListInformation
) : MviBaseViewModel<ClientsViewState>(ClientsViewState.Loading) {

    private var cacheClients: List<Client>? = null
    init {
        launchGeneral {
            loadClientListInformation.invoke().collect {
                cacheClients = it
                _state.value = ClientsViewState.DataLoaded(it)
            }
        }
    }


    fun onSearch(query: String) {
        launchGeneral {
            val filteredList = cacheClients?.filter {
                it.name!!.toLowerCase().contains(query.toLowerCase())
            }
            filteredList?.let {
                _state.value = ClientsViewState.DataLoaded(filteredList)
            }
        }
    }

}