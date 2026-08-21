package com.ingjuanocampo.enfila.data.backend.source

import android.util.Log
import com.enfila.data.backend.ApiClient
import com.enfila.data.backend.models.ApiResponse
import com.enfila.data.backend.models.BackendTipWithStatus
import com.ingjuanocampo.enfila.data.backend.mappers.toDomainTip
import com.ingjuanocampo.enfila.domain.entity.Tip
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BackendTipSource @Inject constructor(
    private val apiClient: ApiClient,
) {
    private val client = apiClient.httpClient

    companion object {
        private const val TAG = "BackendTipSource"
    }

    suspend fun getTipsForUser(userId: String): List<Tip>? {
        val url = "${ApiClient.API_V1}/tips/for-user/$userId"
        return try {
            val response = client.get(url)
            if (response.status.value !in 200..299) {
                Log.w(TAG, "getTipsForUser - HTTP Error: ${response.status}")
                return null
            }
            val apiResponse: ApiResponse<List<BackendTipWithStatus>> = response.body()
            if (apiResponse.success) {
                apiResponse.data
                    ?.map { it.toDomainTip() }
                    ?.sortedBy { it.order }
            } else {
                Log.w(TAG, "getTipsForUser - API Error: ${apiResponse.error}")
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "getTipsForUser - Error: ${e.message}", e)
            null
        }
    }
}
