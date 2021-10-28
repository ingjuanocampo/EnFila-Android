package com.ingjuanocampo.enfila.domain.data.source.template

import com.ingjuanocampo.enfila.domain.data.source.user.emitInContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

class GenericCache<T>: Storage<T> {

    private val cacheList: ArrayList<T> = ArrayList()
    private val shareCacheFlow = MutableSharedFlow<List<T>>()

    override fun save(data: T) {
        cacheList.add(data)
        shareCacheFlow.emitInContext(getData())
    }

    override fun save(data: List<T>) {
        //cacheList.clear()
        cacheList.addAll(data)
        shareCacheFlow.emitInContext(getData())
    }

    override fun getData(): List<T> = cacheList

    override fun observeData(): Flow<List<T>> {
        return flow {
            val observerChannel = Channel<Unit>(Channel.CONFLATED)

            observerChannel.offer(Unit) // Initial signal to perform first query.

            withContext(Dispatchers.Default) {
                try {
                    // Iterate until cancelled, transforming observer signals to query results to
                    // be emitted to the flow.
                    for (signal in observerChannel) {
                        withContext(Dispatchers.Default) { emit(getData()) }
                    }
                } finally {
                }
            }
        }
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