package com.ingjuanocampo.enfila.data.source.user

import com.ingjuanocampo.enfila.data.backend.source.BackendUserSource
import com.ingjuanocampo.enfila.domain.data.source.RemoteSource
import com.ingjuanocampo.enfila.domain.entity.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRemoteSourceBackend @Inject constructor(
    private val backendUserSource: BackendUserSource
) : RemoteSource<User> {
    
    override suspend fun fetchDataAll(id: String): List<User>? {
        return backendUserSource.fetchDataAll(id)
    }
    
    override suspend fun fetchData(id: String): User? {
        return backendUserSource.fetchData(id)
    }
    
    override fun uploadData(data: User): Flow<User?> {
        return backendUserSource.uploadData(data)
    }
    
    override fun uploadData(data: List<User>): Flow<List<User>?> {
        return backendUserSource.uploadData(data)
    }
    
    suspend fun fetchUserByPhone(phone: String): User? {
        return backendUserSource.fetchUserByPhone(phone)
    }
    
    suspend fun createUser(user: User): User? {
        return backendUserSource.createUser(user)
    }
    
    suspend fun deleteUser(id: String): Boolean {
        return backendUserSource.deleteUser(id)
    }
}
