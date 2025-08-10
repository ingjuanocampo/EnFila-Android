package com.ingjuanocampo.enfila.data.source.client

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ingjuanocampo.enfila.data.source.user.basePath
import com.ingjuanocampo.enfila.data.util.fetchProcess
import com.ingjuanocampo.enfila.data.util.fetchProcessMultiples
import com.ingjuanocampo.enfila.data.util.uploadProcess
import com.ingjuanocampo.enfila.data.util.uploadProcessMultiples
import com.ingjuanocampo.enfila.domain.data.source.RemoteSource
import com.ingjuanocampo.enfila.domain.entity.Client
import com.ingjuanocampo.enfila.domain.util.EMPTY_STRING
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

const val clientPath = basePath + "_client"

class ClientRemoteSourceFB
@Inject
constructor() : RemoteSource<Client> {
    private val remote by lazy { Firebase.firestore }

    override suspend fun fetchDataAll(id: String): List<Client>? {
        return remote.fetchProcessMultiples({
            map(it)
        }, clientPath).firstOrNull()
    }

    private fun map(it: Map<String, Any>) =
        Client(
            id = it["id"] as String,
            name = it["name"] as String? ?: EMPTY_STRING,
            shifts = it["shifts"] as List<String>?
        )

    override fun uploadData(data: List<Client>): Flow<List<Client>?> {
        return remote.uploadProcessMultiples({
            return@uploadProcessMultiples mapToRemote(it)
        }, data, clientPath)
    }

    private fun mapToRemote(it: Client) =
        hashMapOf(
            "id" to it.id,
            "name" to it.name,
            "shifts" to it.shifts
        )

    override fun uploadData(data: Client): Flow<Client?> {
        return remote.uploadProcess({
            return@uploadProcess mapToRemote(it)
        }, data, clientPath, data.id)
    }

    override suspend fun fetchData(id: String): Client? {
        return remote.fetchProcess({
            return@fetchProcess map(it)
        }, clientPath, id).firstOrNull()
    }
}
