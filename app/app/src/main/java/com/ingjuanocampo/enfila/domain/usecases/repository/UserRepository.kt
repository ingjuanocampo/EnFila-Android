package com.ingjuanocampo.enfila.domain.usecases.repository

import com.ingjuanocampo.enfila.domain.entity.User
import com.ingjuanocampo.enfila.domain.usecases.repository.base.Repository

interface UserRepository: Repository<User> {

    fun isUserLogged(): Boolean
    suspend fun getCurrent(): User?

}