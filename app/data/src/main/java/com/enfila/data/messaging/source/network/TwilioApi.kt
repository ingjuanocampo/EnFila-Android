package com.enfila.data.messaging.source.network

import com.enfila.data.messaging.source.network.rest.TwilioMessageResponse
import retrofit2.http.*

interface TwilioApi {


    @POST("2010-04-01/Accounts/{accountId}/Messages.json")
    @FormUrlEncoded
    suspend fun postMessage(
        @Header("Authorization") auth: String,
        @Path("accountId") accountId: String,
        @Field("To") to: String,
        @Field("From") from: String,
        @Field("Body") body: String
    ): TwilioMessageResponse

}