package com.ingjuanocampo.enfila.android.home.tips

import androidx.lifecycle.viewModelScope
import com.ingjuanocampo.common.composable.MviBaseViewModel
import com.ingjuanocampo.enfila.android.utils.launchGeneral
import com.ingjuanocampo.enfila.domain.usecases.LoadTipsUC
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ViewModelTips @Inject constructor(
    private val loadTipsUC: LoadTipsUC,
) : MviBaseViewModel<TipsViewState>(TipsViewState()) {

    fun loadTips() {
        viewModelScope.launchGeneral {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val tips = loadTipsUC()
                _state.value = TipsViewState(isLoading = false, tips = tips)
            } catch (e: Exception) {
                _state.value = TipsViewState(
                    isLoading = false,
                    error = e.message ?: "Failed to load tips",
                )
            }
        }
    }
}
