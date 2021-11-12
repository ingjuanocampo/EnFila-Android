package com.ingjuanocampo.enfila.domain.di.data

import android.content.Context
import com.ingjuanocampo.enfila.data.source.client.ClientRemoteSourceFB
import com.ingjuanocampo.enfila.data.source.companysite.CompanyInfoRemoteSource
import com.ingjuanocampo.enfila.data.source.shifts.ShiftsRemoteSourceFirebase
import com.ingjuanocampo.enfila.data.source.user.UserLocalSource
import com.ingjuanocampo.enfila.domain.data.source.companysite.CompanySiteLocalSource
import com.ingjuanocampo.enfila.domain.data.source.companysite.CompanySiteRemoteSource
import com.ingjuanocampo.enfila.domain.data.source.shifts.ShiftLocalSourceGenericCache
import com.ingjuanocampo.enfila.domain.data.source.shifts.ShiftsRemoteSourceImpl
import com.ingjuanocampo.enfila.domain.data.source.template.GenericLocalStoreImp
import com.ingjuanocampo.enfila.domain.data.source.user.UserRemoteImpl
import com.ingjuanocampo.enfila.data.source.user.UserRemoteSource
import com.ingjuanocampo.enfila.domain.data.*
import com.ingjuanocampo.enfila.domain.entity.Client
import com.ingjuanocampo.enfila.domain.usecases.repository.ClientRepository
import com.ingjuanocampo.enfila.domain.usecases.repository.CompanyRepository
import com.ingjuanocampo.enfila.domain.usecases.repository.ShiftRepository
import com.ingjuanocampo.enfila.domain.usecases.repository.UserRepository
import com.ingjuanocampo.enfila.domain.usecases.repository.base.Repository

class DataModule(private val context: Context) {


    val userRepository: UserRepository by lazy {
        UserRepositoryImpl(UserRemoteImpl(UserRemoteSource()), UserLocalSource(context))
    }

    val companySiteRepository: CompanyRepository by lazy {
        CompanyRepositoryImpl(
            remoteSource = CompanySiteRemoteSource(CompanyInfoRemoteSource()),
            localSource = CompanySiteLocalSource()
        )
    }

    val clientRepository: ClientRepository by lazy {
        ClientRepositoryImpl(
            remoteSource = ClientRemoteSourceFB(),
            localSource = GenericLocalStoreImp()
        )
    }

    val shiftsRepository: ShiftRepository by lazy {
        ShiftRepositoryImpl(
            remoteSource = ShiftsRemoteSourceImpl(ShiftsRemoteSourceFirebase()),
            localSource = ShiftLocalSourceGenericCache()
        )
    }

}