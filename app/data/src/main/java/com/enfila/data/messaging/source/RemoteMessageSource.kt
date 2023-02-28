package com.enfila.data.messaging.source

import com.enfila.data.messaging.TwillioCredentials
import com.enfila.data.messaging.source.network.TwilioApi
import com.enfila.data.messaging.source.network.rest.TwilioMessageResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import javax.inject.Inject

class RemoteMessageSource @Inject constructor(private val twillioCredentials: TwillioCredentials) {

    private var api: TwilioApi
    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.twilio.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(TwilioApi::class.java)
    }

    suspend fun sendMessage(to: String, from: String, body: String): TwilioMessageResponse {
        val token = "${twillioCredentials.sid}:${
            twillioCredentials.token}"
        val tokenEncode = Base64.getEncoder().encodeToString(token.toByteArray())
        // (Account id:auth token) Encoded in base64

        return api.postMessage(
            auth = "Basic $tokenEncode",
            accountId = twillioCredentials.sid,
            to = to,
            from = from,
            body = body,
        )
    }
}
