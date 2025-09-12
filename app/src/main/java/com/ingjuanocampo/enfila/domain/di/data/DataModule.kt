package com.ingjuanocampo.enfila.domain.di.data

import com.ingjuanocampo.enfila.data.source.client.ClientRemoteSourceBackend
import com.ingjuanocampo.enfila.data.source.client.ClientRemoteSourceFB
import com.ingjuanocampo.enfila.data.source.shifts.ShiftsRemoteSourceBackend
import com.ingjuanocampo.enfila.data.source.user.UserLocalSource
import com.ingjuanocampo.enfila.data.source.user.UserRemoteSourceBackend
import com.ingjuanocampo.enfila.domain.data.*
import com.ingjuanocampo.enfila.domain.data.source.companysite.CompanySiteLocalSource
import com.ingjuanocampo.enfila.domain.data.source.companysite.CompanySiteRemoteSource
import com.ingjuanocampo.enfila.domain.data.source.shifts.ShiftLocalSourceGenericCache
import com.ingjuanocampo.enfila.domain.data.source.shifts.ShiftsRemoteSourceImpl
import com.ingjuanocampo.enfila.domain.data.source.template.GenericLocalStoreImp
import com.ingjuanocampo.enfila.domain.data.source.user.UserRemoteImpl
import com.ingjuanocampo.enfila.domain.usecases.repository.ClientRepository
import com.ingjuanocampo.enfila.domain.usecases.repository.CompanyRepository
import com.ingjuanocampo.enfila.domain.usecases.repository.ShiftRepository
import com.ingjuanocampo.enfila.domain.usecases.repository.UserRepository
import com.ingjuanocampo.enfila.domain.usecases.repository.base.Repository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import javax.inject.Qualifier
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DataModule {
    @Singleton
    @Provides
    fun bindsUserRepository(
        userRemoteBackend: UserRemoteSourceBackend,
        userLocalSource: UserLocalSource,
    ): UserRepository {
        return UserRepositoryImpl(userRemoteBackend, userLocalSource)
    }

    @Singleton
    @Provides
    fun bindsCompanyRepository(
        companySiteRemoteSource: CompanySiteRemoteSource,
        companySiteLocalSource: CompanySiteLocalSource,
    ): CompanyRepository {
        return CompanyRepositoryImpl(companySiteRemoteSource, companySiteLocalSource)
    }

    @Singleton
    @Provides
    fun bindClientRepository(clientRemoteSourceBackend: ClientRemoteSourceBackend): ClientRepository {
        return ClientRepositoryImpl(clientRemoteSourceBackend, GenericLocalStoreImp())
    }

    @Singleton
    @Provides
    fun providesShiftRepository(
        shiftsRemoteSourceBackend: ShiftsRemoteSourceBackend,
        ShiftLocalSourceGenericCache: ShiftLocalSourceGenericCache,
    ): ShiftRepository {
        return ShiftRepositoryImpl(shiftsRemoteSourceBackend, ShiftLocalSourceGenericCache)
    }
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class DELETABLE

@InstallIn(SingletonComponent::class)
@Module
internal interface DeletableModule {
    @DELETABLE
    @Binds
    @IntoSet
    fun bindsUserRemoteImpl(userRemoteImpl: UserRepository): Repository<*>

    @DELETABLE
    @Binds
    @IntoSet
    fun bindsCompanyRepository(companyRepository: CompanyRepository): Repository<*>

    @DELETABLE
    @Binds
    @IntoSet
    fun bindsClientRepository(ClientRepository: ClientRepository): Repository<*>

    @DELETABLE
    @Binds
    @IntoSet
    fun bindsShiftRepository(ShiftRepository: ShiftRepository): Repository<*>
}
