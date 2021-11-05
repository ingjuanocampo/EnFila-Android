package com.ingjuanocampo.enfila.domain.data.source.shifts.mock

import com.ingjuanocampo.enfila.domain.data.source.shifts.ShiftLocalSource
import com.ingjuanocampo.enfila.domain.entity.Shift
import com.ingjuanocampo.enfila.domain.entity.ShiftFactory
import com.ingjuanocampo.enfila.domain.entity.ShiftState
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

// Constant mock data
private val list = arrayListOf(
    ShiftFactory.createWaiting(1, "3137550991", "Fast please", ""),
    ShiftFactory.createWaiting(2, "3137550992", "Fast please",""),
    ShiftFactory.createWaiting(3, "3137550993", "Fast please",""),
    ShiftFactory.createWaiting(4, "3137550994", "Fast please",""),
    ShiftFactory.createWaiting(5, "3137550995", "Fast please","")
)
/*

class ShiftsMockSource : ShiftLocalSource {

    val flow = MutableSharedFlow<List<Shift>>(3)

    init {
        GlobalScope.launch {
            flow.emit(list)
        }
    }

    override fun getClosestShift(): Flow<Shift?> {
        return flow.map { list ->
            list.firstOrNull { it.state == ShiftState.WAITING }
        }
    }

    override fun getLastShift(): Flow<Shift?> {
        return flow {
            emit(list.lastOrNull())
        }
    }

    override fun getCallingShift(): Flow<Shift?> {
        return flow.map { list ->
            list.firstOrNull { it.state == ShiftState.CALLING }
        }
    }

    override suspend fun createOrUpdate(data: List<Shift>) {
        data.forEach {
            if (list.contains(it)) {
                val index = list.indexOf(it)
                list.removeAt(index)
                list.add(index, it)
            } else list.add(it)
        }
        flow.emit(list)
    }

    override suspend fun delete(dataToDelete: List<Shift>) {
        flow.tryEmit(list)
    }

    override fun getAllObserveData(): Flow<List<Shift>> {
        return flow
    }

    override suspend fun getAllData(): List<Shift> {
        return list
    }

    override suspend fun getById(id: String): List<Shift> {
        return list.filter { it.id == id }
    }

    override suspend fun delete(id: String) {
        TODO("Not yet implemented")
    }
}*/
