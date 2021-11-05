package com.ingjuanocampo.enfila.domain.data.util

import java.util.*
import kotlin.collections.HashMap
import kotlin.jvm.Synchronized

class RateLimiter<in KEY>(timeOutSeconds: Int) {
    private val timestamps = HashMap<KEY, Long>()
    private val timeout = timeOutSeconds * 1000

    @Synchronized
    fun shouldFetch(key: KEY): Boolean {
        val lastFetched = timestamps[key]
        val now = now()
        if (lastFetched == null) {
            timestamps[key] = now
            return true
        }
        if (now - lastFetched > timeout) {
            timestamps[key] = now
            return true
        }
        return false
    }

    private fun now() = Date().time

    @Synchronized
    fun reset(key: KEY) {
        timestamps.remove(key)
    }
}
