package com.ingjuanocampo.enfila.data.source.user

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.ingjuanocampo.enfila.domain.data.source.LocalSource
import com.ingjuanocampo.enfila.domain.data.source.db.realm.entity.toEntity
import com.ingjuanocampo.enfila.domain.data.source.db.realm.entity.toList
import com.ingjuanocampo.enfila.domain.entity.User
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserLocalSource @Inject constructor(@ApplicationContext private val context: Context) : LocalSource<User> {

    val Context.userPreferencesDataStore: DataStore<Preferences> by preferencesDataStore("settings")

    private val USER_NAME = stringPreferencesKey("user_name")
    private val USER_ID = stringPreferencesKey("user_id")
    private val USER_PHONE = stringPreferencesKey("user_phone")
    private val USER_COMPANY_IDS = stringPreferencesKey("user_company_ids")

    override suspend fun createOrUpdate(data: User) {
        context.userPreferencesDataStore.edit {
            it[USER_NAME] = data.name.orEmpty()
            it[USER_ID] = data.id
            it[USER_PHONE] = data.phone
            it[USER_COMPANY_IDS] = data.companyIds.toEntity()
        }
    }

    override suspend fun delete(dataToDelete: User) {
        context.userPreferencesDataStore.edit {
            it[USER_NAME] = ""
            it[USER_ID] = ""
            it[USER_PHONE] = ""
            it[USER_COMPANY_IDS] = ""
        }
    }

    override suspend fun delete(id: String) {
        context.userPreferencesDataStore.edit {
            it[USER_NAME] = ""
            it[USER_ID] = ""
            it[USER_PHONE] = ""
            it[USER_COMPANY_IDS] = ""
        }
    }

    override fun getAllObserveData(): Flow<List<User>?> {
        return context.userPreferencesDataStore.data.map {
            User(
                id = it[USER_ID].orEmpty(),
                name = it[USER_NAME],
                phone = it[USER_PHONE].orEmpty(),
                companyIds = it[USER_COMPANY_IDS]?.toList(),
            )
        }.map { listOf(it) }
    }

    override suspend fun getAllData(): List<User>? {
        return getAllObserveData().firstOrNull()
    }

    override suspend fun getById(id: String): User? {
        return getAllObserveData().firstOrNull()?.firstOrNull()
    }

    override suspend fun createOrUpdate(data: List<User>) {
        data.forEach {
            createOrUpdate(it)
        }
    }
}
