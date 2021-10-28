package com.ingjuanocampo.enfila.domain.data.util

import kotlinx.datetime.Clock
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

    private fun now() = Clock.System.now().epochSeconds

    @Synchronized
    fun reset(key: KEY) {
        timestamps.remove(key)
    }
}
