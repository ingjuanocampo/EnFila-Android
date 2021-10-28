package com.ingjuanocampo.enfila.domain.data.source.shifts

import com.ingjuanocampo.enfila.domain.data.source.db.realm.Database
import com.ingjuanocampo.enfila.domain.data.source.db.realm.entity.ShiftEntity
import com.ingjuanocampo.enfila.domain.data.source.db.realm.entity.toEntity
import com.ingjuanocampo.enfila.domain.data.source.db.realm.entity.toModel
import com.ingjuanocampo.enfila.domain.entity.Shift
import com.ingjuanocampo.enfila.domain.entity.ShiftState
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ShiftLocalSourceImp(val database: Database) : ShiftLocalSource {

    private var isSubscribed: Boolean = false

    private val sharedFlow = MutableSharedFlow<List<Shift>?>()
    private val initialFlow = flow {
        val initialValues = database.get().objects<ShiftEntity>()
        if (!isSubscribed) {
            initialValues.observe { result ->
                GlobalScope.launch {
                    sharedFlow.emit(result.map { it.toModel() })
                }
            }
        }
        isSubscribed = true
        emit(initialValues.query().map { it.toModel() })// emit initial value
    }

    private fun observeAll(): Flow<List<Shift>?> {
        return merge(initialFlow, sharedFlow)
    }

    override suspend fun  getClosestShift(): Shift? {
        return getAllData()?.firstOrNull{ it.state == ShiftState.WAITING }
    }

    override suspend fun getLastShift(): Shift? {
        return getAllData()?.lastOrNull()
    }

    override suspend fun getCallingShift(): Shift? {
        return getAllData()?.firstOrNull { it.state == ShiftState.CALLING }

    }

    override suspend fun createOrUpdate(data: List<Shift>) {
        database.get().writeBlocking {
            data.forEach {
                copyToRealm(it.toEntity())
            }
        }
        if (isSubscribed) {
            val initialValues = getAllData()
            sharedFlow.emit(initialValues)// emit initial value
        }
    }

    override suspend fun delete(dataToDelete: List<Shift>) {
        dataToDelete.forEach {
            database.get().objects<ShiftEntity>().query("id = $0", it.id)
        }
    }

    override suspend fun delete(id: String) {
        database.get().objects<ShiftEntity>().query("id = $0", id)
    }

    override fun getAllObserveData(): Flow<List<Shift>?> {
        return observeAll()
    }

    override suspend fun getAllData(): List<Shift>? {
        return database.get().objects<ShiftEntity>().query().map { it.toModel() }
    }

    override suspend fun getById(id: String): List<Shift>? {
        return database.get().objects<ShiftEntity>().query("id = $0", id).map { it.toModel() }
    }
}