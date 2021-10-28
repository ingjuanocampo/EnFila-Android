package com.ingjuanocampo.enfila.domain.di.domain

import com.ingjuanocampo.enfila.domain.Platform
import com.ingjuanocampo.enfila.domain.di.data.DataModule
import com.ingjuanocampo.enfila.domain.state.AppStateProvider
import com.ingjuanocampo.enfila.domain.usecases.HomeUC
import com.ingjuanocampo.enfila.domain.usecases.LoadInitialInfoUC
import com.ingjuanocampo.enfila.domain.usecases.ShiftInteractions
import com.ingjuanocampo.enfila.domain.usecases.signing.SignInUC
import com.ingjuanocampo.enfila.domain.usecases.list.ListUC

object DomainModule {

    var appPlatform: Platform? = null
        set(value) {
            DataModule.appPlatform = value
        }

    private val dataModule = DataModule

    fun providesShiftInteractions(): ShiftInteractions =
        ShiftInteractions(dataModule.shiftsRepository, dataModule.clientRepository)

    fun provideHomeUC() = HomeUC(
        DataModule.companySiteRepository, dataModule.userRepository, dataModule.shiftsRepository,
        providesShiftInteractions()
    )

    fun provideLoadInitialInfo() =
        LoadInitialInfoUC(dataModule.userRepository, dataModule.shiftsRepository, dataModule.companySiteRepository)

    fun provideListUC() = ListUC(DataModule.shiftsRepository, providesShiftInteractions())

    fun provideSignUC(appStateProvider: AppStateProvider) = SignInUC(
        dataModule.userRepository,
        dataModule.companySiteRepository,
        appStateProvider,
        dataModule.shiftsRepository
    )

    fun provideIsUserLoggedMethod(): () -> Boolean {
        return {
            dataModule.userRepository.isUserLogged()
        }
    }


}