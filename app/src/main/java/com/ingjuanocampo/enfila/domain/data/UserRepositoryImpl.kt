package com.ingjuanocampo.enfila.domain.data

import com.ingjuanocampo.enfila.domain.data.source.LocalSource
import com.ingjuanocampo.enfila.domain.data.source.RemoteSource
import com.ingjuanocampo.enfila.domain.entity.User
import com.ingjuanocampo.enfila.domain.usecases.repository.UserRepository
import com.ingjuanocampo.enfila.domain.util.EMPTY_STRING

class UserRepositoryImpl(
    private val remote: RemoteSource<User>,
    private val localSource: LocalSource<User>,
) : UserRepository, RepositoryImp<User>(remote, localSource) {

    override fun isUserLogged() = id.isNullOrBlank().not()
    override suspend fun getCurrent(): User? {
        val currentUser = localSource.getAllData()?.firstOrNull()
        this.id = currentUser?.id ?: EMPTY_STRING
        return currentUser
    }

    override suspend fun deleteAll() {
        super.deleteAll()
        this.id = ""
    }
}
