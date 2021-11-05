package com.ingjuanocampo.enfila.domain.data.source.user

import com.ingjuanocampo.enfila.domain.data.source.RemoteSource
import com.ingjuanocampo.enfila.domain.entity.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.firstOrNull

class UserRemoteImpl(private val userRemoteSource: UserRemoteSource): RemoteSource<User> {

     override suspend fun fetchData(id: String): User? =  userRemoteSource.fetchData(id).firstOrNull()

     override fun uploadData(data: User) = userRemoteSource.updateData(data)
}
