package com.ingjuanocampo.enfila.domain.usecases.repository

import com.ingjuanocampo.enfila.domain.entity.User
import com.ingjuanocampo.enfila.domain.usecases.repository.base.Repository
import kotlinx.coroutines.flow.Flow

interface UserRepository: Repository<User> {

    fun getFetchAndObserve(): Flow<User?>

    fun isUserLogged(): Boolean
    suspend fun getCurrent(): User?

}