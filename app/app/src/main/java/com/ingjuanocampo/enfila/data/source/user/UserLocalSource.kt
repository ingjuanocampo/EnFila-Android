package com.ingjuanocampo.enfila.data.source.user

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.ingjuanocampo.enfila.domain.data.source.LocalSource
import com.ingjuanocampo.enfila.domain.data.source.db.realm.entity.toUser
import com.ingjuanocampo.enfila.domain.entity.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map


class UserLocalSource(private val context: Context): LocalSource<User> {


    val Context.userPreferencesDataStore: DataStore<Preferences> by preferencesDataStore("settings")

    private val USER_DATA = stringPreferencesKey("user_data")


    override suspend fun createOrUpdate(data: User) {
        context.userPreferencesDataStore.edit {
            it[USER_DATA] = data.toString()
        }
    }

    override suspend fun delete(dataToDelete: User) {
        context.userPreferencesDataStore.edit {
            it[USER_DATA] = ""
        }
    }

    override suspend fun delete(id: String) {
        context.userPreferencesDataStore.edit {
            it[USER_DATA] = ""
        }
    }

    override fun getAllObserveData(): Flow<User?> {
        return context.userPreferencesDataStore.data.map {
            it[USER_DATA]?.toUser()
        }
    }

    override suspend fun getAllData(): User? {
        return getAllObserveData().firstOrNull()
    }

    override suspend fun getById(id: String): User? {
        return getAllObserveData().firstOrNull()
    }


}
