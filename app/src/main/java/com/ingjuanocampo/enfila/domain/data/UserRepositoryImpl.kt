package com.ingjuanocampo.enfila.domain.data

import com.ingjuanocampo.enfila.data.source.user.UserRemoteSourceBackend
import com.ingjuanocampo.enfila.domain.data.source.LocalSource
import com.ingjuanocampo.enfila.domain.data.source.RemoteSource
import com.ingjuanocampo.enfila.domain.entity.User
import com.ingjuanocampo.enfila.domain.usecases.repository.UserRepository
import com.ingjuanocampo.enfila.domain.util.EMPTY_STRING


class UserRepositoryImpl(
    private val remote: RemoteSource<User>,
    private val localSource: LocalSource<User>,
    private val userRemoteSourceBackend: UserRemoteSourceBackend? = null, // Optional for backend operations
) : UserRepository, RepositoryImp<User>(remote, localSource) {

    override suspend fun refresh() {
        val response = remote.fetchData(id)
        response?.let {
            localSource.createOrUpdate(it)
        }
    }

    override fun isUserLogged() = id.isNullOrBlank().not()

    override suspend fun getCurrent(): User? {
        val currentUser = localSource.getAllData()?.firstOrNull()
        this.id = currentUser?.id ?: EMPTY_STRING
        return currentUser
    }

    override suspend fun deleteAll() {
        localSource.deleteAll()
        this.id = ""
    }

    override suspend fun createUser(user: User): User? {
        return userRemoteSourceBackend?.createUser(user)?.also { createdUser ->
            // Cache the created user locally
            localSource.createOrUpdate(createdUser)
        }
    }

}
