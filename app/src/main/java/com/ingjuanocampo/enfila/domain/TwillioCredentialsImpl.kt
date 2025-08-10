package com.ingjuanocampo.enfila.domain

import com.enfila.data.messaging.TwillioCredentials
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TwillioCredentialsImpl
@Inject
constructor() : TwillioCredentials {
    private val remoteConfig = Firebase.remoteConfig

    init {
        remoteConfig.run {
            setConfigSettingsAsync(
                remoteConfigSettings {
                    minimumFetchIntervalInSeconds = 20
                }
            )
            fetchAndActivate()
        }
    }

    override val token: String
        get() = remoteConfig.getString("twilio_token")
    override val sid: String
        get() = remoteConfig.getString("twilio_sid")
}
