package com.ingjuanocampo.enfila.data.source.user

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ingjuanocampo.enfila.data.util.fetchProcess
import com.ingjuanocampo.enfila.data.util.uploadProcess
import com.ingjuanocampo.enfila.domain.entity.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

const val basePath = "enfila_v1"

private const val userPath = basePath + "_users"

class UserRemoteSource @Inject constructor() {

    private val db by lazy { Firebase.firestore }

    fun fetchData(id: String): Flow<User?> {
        return db.fetchProcess({ data ->
            return@fetchProcess User(
                id = id,
                name = data.get("name") as String?,
                phone = data.get("phone") as String,
                companyIds = data.get("companyIds") as? List<String>,
            )
        }, userPath, id)
    }

    fun updateData(data: User): Flow<User?> {
        return db.uploadProcess({
            return@uploadProcess hashMapOf(
                "name" to data.name,
                "phone" to data.phone,
                "companyIds" to data.companyIds,
            )
        }, data, userPath, data.id)
    }
}
