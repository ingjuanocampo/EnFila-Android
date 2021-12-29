package com.enfila.data.messaging.source.network.rest

import com.google.gson.annotations.SerializedName

class TwilioMessageResponse(
    @SerializedName("sid") var sid: String? = null,
    @SerializedName("date_created") var dateCreated: String? = null,
    @SerializedName("date_updated") var dateUpdated: String? = null,
    @SerializedName("date_sent") var dateSent: String? = null,
    @SerializedName("account_sid") var accountSid: String? = null,
    @SerializedName("to") var to: String? = null,
    @SerializedName("from") var from: String? = null,
    @SerializedName("messaging_service_sid") var messagingServiceSid: String? = null,
    @SerializedName("body") var body: String? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("num_segments") var numSegments: String? = null,
    @SerializedName("num_media") var numMedia: String? = null,
    @SerializedName("direction") var direction: String? = null,
    @SerializedName("api_version") var apiVersion: String? = null,
    @SerializedName("price") var price: String? = null,
    @SerializedName("price_unit") var priceUnit: String? = null,
    @SerializedName("error_code") var errorCode: String? = null,
    @SerializedName("error_message") var errorMessage: String? = null,
    @SerializedName("uri") var uri: String? = null,
)