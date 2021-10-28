package com.ingjuanocampo.enfila.domain.data.source.db.realm

import com.ingjuanocampo.enfila.domain.Platform
import com.ingjuanocampo.enfila.domain.data.source.db.realm.entity.ShiftEntity
import com.ingjuanocampo.enfila.domain.data.source.db.realm.entity.UserEntity
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Database(private val platform: Platform) {

    private fun realm(): Realm {
        val configuration = RealmConfiguration(schema = setOf(UserEntity::class, ShiftEntity::class))
        return Realm.open(configuration)
    }

    private var realm: Realm ? = null

    fun get(): Realm {
        realm?.close()
        realm = realm()
        return realm!!
    }


}

fun Realm.runSafe() {

    this.close()
}