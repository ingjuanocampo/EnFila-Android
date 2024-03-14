package com.ingjuanocampo.enfila.domain.data.source.template

import com.ingjuanocampo.enfila.domain.entity.IdentifyObject
import com.ingjuanocampo.enfila.domain.util.emitInContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

class GenericCache<T : IdentifyObject> : Storage<T> {
    private val cacheList: HashMap<String, T> = HashMap<String, T>()
    private val shareCacheFlow = MutableSharedFlow<List<T>>()

    override fun save(data: T) {
        cacheList[data.id] = data
        shareCacheFlow.emitInContext(getData())
    }

    override suspend fun save(data: List<T>) {
        data.forEach {
            cacheList[it.id] = it
        }
        shareCacheFlow.emit(getData())
    }

    override fun getData(): List<T> = cacheList.values.toList()

    override fun observeData(): Flow<List<T>> {
        return merge(
            flow {
                val observerChannel = Channel<Unit>(Channel.CONFLATED)

                observerChannel.trySend(Unit) // Initial signal to perform first query.

                try {
                    // Iterate until cancelled, transforming observer signals to query results to
                    // be emitted to the flow.
                    for (signal in observerChannel) {
                        emit(getData())
                    }
                } finally {
                }
            },
            shareCacheFlow,
        )
    }

    override fun deleteAll() {
        cacheList.clear()
        shareCacheFlow.tryEmit(getData())
    }

    override fun deleteById(id: String) {
        /*cacheList.indexOf(id)?.let {
            cacheList.removeAt(it)
        }
        shareCacheFlow.tryEmit(getData())*/
    }
}
