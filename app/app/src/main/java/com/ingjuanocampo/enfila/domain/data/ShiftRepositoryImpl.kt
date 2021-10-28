package com.ingjuanocampo.enfila.domain.data

import com.ingjuanocampo.enfila.domain.data.source.RemoteSource
import com.ingjuanocampo.enfila.domain.data.source.shifts.ShiftLocalSource
import com.ingjuanocampo.enfila.domain.entity.Shift
import com.ingjuanocampo.enfila.domain.usecases.repository.ShiftRepository

class ShiftRepositoryImpl(
    private val remoteSource: RemoteSource<List<Shift>>,
    private val localSource: ShiftLocalSource
): ShiftRepository, RepositoryImp<List<Shift>>(remoteSource, localSource) {

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