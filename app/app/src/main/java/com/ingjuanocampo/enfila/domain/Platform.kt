package com.ingjuanocampo.enfila.domain

import android.content.Context

class Platform(context: Any) {
    val platform: String = "Android ${android.os.Build.VERSION.SDK_INT}"
    val basePath = (context as Context).filesDir.toString() + "myfile/"
}