package com.ingjuanocampo.enfila.domain.data.source.user

import com.ingjuanocampo.enfila.domain.data.source.LocalSource
import com.ingjuanocampo.enfila.domain.data.source.db.realm.Database
import com.ingjuanocampo.enfila.domain.data.source.db.realm.entity.UserEntity
import com.ingjuanocampo.enfila.domain.data.source.db.realm.entity.toEntity
import com.ingjuanocampo.enfila.domain.data.source.db.realm.entity.toModel
import com.ingjuanocampo.enfila.domain.entity.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class UserLocalSource(val database: Database): LocalSource<User> {


    override suspend fun createOrUpdate(data: User) {
        database.get().writeBlocking {
            copyToRealm(data?.toEntity()!!)
        }
    }

    override suspend fun delete(dataToDelete: User) {
        database.get().objects<UserEntity>().delete()
    }

    override suspend fun delete(id: String) {
        database.get().objects<UserEntity>().query("id = $0", id)
    }

    override fun getAllObserveData(): Flow<User?> {
        return flow {
            database.get().objects<UserEntity>().query().observe {
                GlobalScope.launch {
                    emit((it.firstOrNull()?.toModel()))
                }
            }
            emit(getAllData())
        }
    }

    override suspend fun getAllData(): User? {
        return try { database.get().objects<UserEntity>().firstOrNull()?.toModel()} catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    override suspend fun getById(id: String): User? {
        return try {database.get().objects<UserEntity>().query("id = $0", id).firstOrNull()?.toModel()} catch (e: Exception) {null}
    }
}

fun <T>MutableSharedFlow<T>.emitInContext(data: T) {
    val shareFlow = this
    GlobalScope.launch {
        shareFlow.emit(data)
    }
}

