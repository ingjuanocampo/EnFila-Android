package com.ingjuanocampo.enfila.domain.data.source.user

import com.ingjuanocampo.enfila.domain.data.source.RemoteSource
import com.ingjuanocampo.enfila.domain.entity.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.firstOrNull

class UserRemoteImpl(private val userRemoteSource: UserRemoteSource): RemoteSource<User> {

     override fun createOrUpdateFlow(data: User): Flow<User?> = userRemoteSource.updateData(data)

     override fun fetchInfoFlow(id: String): Flow<User?> = userRemoteSource.fetchData(id)

     override suspend fun fetchData(id: String): User? =  userRemoteSource.fetchData(id).firstOrNull()

     override suspend fun createOrUpdate(data: User) = userRemoteSource.updateData(data).collect()
}

expect class UserRemoteSource() {
     fun fetchData(id: String): Flow<User?>
     fun updateData(data: User): Flow<User?>

}