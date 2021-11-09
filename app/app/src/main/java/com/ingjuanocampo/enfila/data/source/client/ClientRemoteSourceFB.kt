package com.ingjuanocampo.enfila.data.source.client

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ingjuanocampo.enfila.data.util.fetchProcessMultiples
import com.ingjuanocampo.enfila.data.util.uploadProcessMultiples
import com.ingjuanocampo.enfila.domain.data.source.RemoteSource
import com.ingjuanocampo.enfila.domain.entity.Client
import com.ingjuanocampo.enfila.domain.util.EMPTY_STRING
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map


class ClientRemoteSourceFB: RemoteSource<Client> {
    private val remote by lazy { Firebase.firestore }

    override suspend fun fetchDataAll(id: String): List<Client>? {
        return remote.fetchProcessMultiples({
            Client(
                id = it["id"] as String,
                name = it["name"] as String? ?: EMPTY_STRING,
                shifts = it["shifts"] as List<String>?
            )
        }, getPath(id)).firstOrNull()
    }

    private fun getPath(contactId: String) = "$contactId"


    override fun uploadData(data: List<Client>): Flow<List<Client>?> {
        return remote.uploadProcessMultiples({
            return@uploadProcessMultiples mapToRemote(it)
        }, data, getPath(data.firstOrNull()!!.id))

    }

    private fun mapToRemote(it: Client) = hashMapOf(
        "id" to it.id,
        "name" to it.name,
        "shifts" to it.shifts
    )

    override fun uploadData(data: Client): Flow<Client?> {
        return uploadData(listOf(data)).map { data }
    }

    override suspend fun fetchData(id: String): Client? {
        TODO("Not yet implemented")
    }


}