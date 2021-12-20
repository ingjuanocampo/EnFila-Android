package com.ingjuanocampo.enfila.domain.di.domain

import android.content.Context
import com.ingjuanocampo.enfila.domain.Platform
import com.ingjuanocampo.enfila.domain.di.data.DataModule
import com.ingjuanocampo.enfila.domain.state.AppStateProvider
import com.ingjuanocampo.enfila.domain.usecases.FinishShiftUC
import com.ingjuanocampo.enfila.domain.usecases.HomeUC
import com.ingjuanocampo.enfila.domain.usecases.LoadInitialInfoUC
import com.ingjuanocampo.enfila.domain.usecases.ShiftInteractions
import com.ingjuanocampo.enfila.domain.usecases.signing.SignInUC
import com.ingjuanocampo.enfila.domain.usecases.list.ListUC

class DomainModule(private val context: Context) {


    private val dataModule by lazy { DataModule(context) }

    fun providesShiftInteractions(): ShiftInteractions =
        ShiftInteractions(dataModule.shiftsRepository, dataModule.clientRepository)

    fun provideClientRepository() = dataModule.clientRepository

    fun provideHomeUC() = HomeUC(
        dataModule.companySiteRepository, dataModule.userRepository, dataModule.shiftsRepository,
        providesShiftInteractions()
    )

    fun provideFinishUC() = FinishShiftUC(dataModule.shiftsRepository, providesShiftInteractions())

    fun provideLoadInitialInfo() =
        LoadInitialInfoUC(dataModule.userRepository, dataModule.shiftsRepository, dataModule.companySiteRepository, dataModule.clientRepository)

    fun provideListUC() = ListUC(dataModule.shiftsRepository, providesShiftInteractions())

    fun provideSignUC(appStateProvider: AppStateProvider) = SignInUC(
        dataModule.userRepository,
        dataModule.companySiteRepository,
        appStateProvider,
        dataModule.shiftsRepository,
        dataModule.clientRepository
    )

    fun provideIsUserLoggedMethod(): () -> Boolean {
        return {
            dataModule.userRepository.isUserLogged()
        }
    }


}