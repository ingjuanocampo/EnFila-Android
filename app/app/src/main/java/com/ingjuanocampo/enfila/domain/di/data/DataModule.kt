package com.ingjuanocampo.enfila.domain.di.data

import com.ingjuanocampo.enfila.domain.Platform
import com.ingjuanocampo.enfila.domain.data.CompanyRepositoryImpl
import com.ingjuanocampo.enfila.domain.data.RepositoryImp
import com.ingjuanocampo.enfila.domain.data.ShiftRepositoryImpl
import com.ingjuanocampo.enfila.domain.data.UserRepositoryImpl
import com.ingjuanocampo.enfila.domain.data.source.companysite.CompanyInfoRemoteSource
import com.ingjuanocampo.enfila.domain.data.source.companysite.CompanySiteLocalSource
import com.ingjuanocampo.enfila.domain.data.source.companysite.CompanySiteRemoteSource
import com.ingjuanocampo.enfila.domain.data.source.contact.ContactMockSource
import com.ingjuanocampo.enfila.domain.data.source.contact.ContactRemoteSource
import com.ingjuanocampo.enfila.domain.data.source.db.realm.Database
import com.ingjuanocampo.enfila.domain.data.source.shifts.ShiftLocalSourceGenericCache
import com.ingjuanocampo.enfila.domain.data.source.shifts.ShiftLocalSourceImp
import com.ingjuanocampo.enfila.domain.data.source.shifts.ShiftsRemoteSourceFirebase
import com.ingjuanocampo.enfila.domain.data.source.shifts.ShiftsRemoteSourceImpl
import com.ingjuanocampo.enfila.domain.data.source.user.UserLocalSource
import com.ingjuanocampo.enfila.domain.data.source.user.UserRemoteImpl
import com.ingjuanocampo.enfila.domain.data.source.user.UserRemoteSource
import com.ingjuanocampo.enfila.domain.entity.Client
import com.ingjuanocampo.enfila.domain.usecases.repository.CompanyRepository
import com.ingjuanocampo.enfila.domain.usecases.repository.ShiftRepository
import com.ingjuanocampo.enfila.domain.usecases.repository.UserRepository
import com.ingjuanocampo.enfila.domain.usecases.repository.base.Repository

internal object DataModule {

    var appPlatform : Platform? = null

    val database by lazy {
        appPlatform?.let { Database(it) }
    }

    val userRepository: UserRepository by lazy {
        UserRepositoryImpl(UserRemoteImpl(UserRemoteSource()), UserLocalSource(database!!) )
    }

    val companySiteRepository: CompanyRepository by lazy {
        CompanyRepositoryImpl(remoteSource = CompanySiteRemoteSource(CompanyInfoRemoteSource()),
        localSource = CompanySiteLocalSource())
    }

    val clientRepository: Repository<List<Client>> by lazy {
        RepositoryImp(remoteSource = ContactRemoteSource(),
            localSource = ContactMockSource())
    }

    val shiftsRepository: ShiftRepository by lazy {
        ShiftRepositoryImpl(remoteSource = ShiftsRemoteSourceImpl(ShiftsRemoteSourceFirebase()),
            localSource = ShiftLocalSourceGenericCache()
        )
    }

}