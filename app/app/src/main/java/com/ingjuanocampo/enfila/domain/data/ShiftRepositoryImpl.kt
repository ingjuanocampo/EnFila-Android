package com.ingjuanocampo.enfila.domain.data

import com.ingjuanocampo.enfila.domain.data.source.RemoteSource
import com.ingjuanocampo.enfila.domain.data.source.shifts.ShiftLocalSource
import com.ingjuanocampo.enfila.domain.entity.Shift
import com.ingjuanocampo.enfila.domain.usecases.repository.ShiftRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ShiftRepositoryImpl(
    private val remoteSource: RemoteSource<Shift>,
    private val localSource: ShiftLocalSource
): ShiftRepository, RepositoryImp<Shift>(remoteSource, localSource) {

    override fun getAllObserveData(): Flow<List<Shift>?> {
        return super.getAllObserveData().map { it?.sortedBy { shift->  shift.number } }
    }

    override suspend fun loadAllData(): List<Shift>? {
        return super.loadAllData()?.sortedBy { it.number }
    }

    override suspend fun getClosestShift(): Shift? {
        return localSource.getClosestShift()
    }

    override suspend fun getLastShift(): Shift? {
        return localSource.getLastShift()
    }

    override suspend fun getCallingShift(): Shift? {
        return localSource.getCallingShift()
    }
}