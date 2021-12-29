package com.enfila.data.messaging.source

import com.enfila.data.messaging.source.network.TwilioApi
import com.enfila.data.messaging.source.network.rest.TwilioMessageResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


private val ACCOUNT_SID = "AC18b5f78b3657180e449ee434e37b96b8"
private val AUTH_TOKEN = "c81baf6543ef1a9c8f5304ca396c22fa"
private val ENCODED_TOKEN = "QUMxOGI1Zjc4YjM2NTcxODBlNDQ5ZWU0MzRlMzdiOTZiODpjODFiYWY2NTQzZWYxYTljOGY1MzA0Y2EzOTZjMjJmYQ==" // (Account id + auth token) Encoded in base64
private val TOKEN = "Basic $ENCODED_TOKEN"

class RemoteMessageSource {

    private var api: TwilioApi
    init {

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.twilio.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(TwilioApi::class.java)

    }

    suspend fun sendMessage(to: String, from: String, body: String): TwilioMessageResponse {
        return api.postMessage(TOKEN, ACCOUNT_SID, to, from, body)
    }



}