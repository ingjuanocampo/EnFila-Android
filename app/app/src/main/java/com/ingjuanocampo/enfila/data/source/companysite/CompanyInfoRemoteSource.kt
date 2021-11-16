package com.ingjuanocampo.enfila.data.source.companysite

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ingjuanocampo.enfila.data.util.fetchProcess
import com.ingjuanocampo.enfila.data.util.uploadProcess
import com.ingjuanocampo.enfila.data.source.user.basePath
import com.ingjuanocampo.enfila.domain.entity.CompanySite
import kotlinx.coroutines.flow.Flow

const val companyInfoPath = basePath + "_company"

class CompanyInfoRemoteSource constructor() {

    val db by lazy { Firebase.firestore }
    fun fetchData(id: String): Flow<CompanySite?> {
        return db.fetchProcess({ data ->
            return@fetchProcess CompanySite(
                id = id,
                name = data["name"] as String?,
                shiftsIdList = data["shiftList"] as List<String>?
            )
        }, companyInfoPath, id)
    }

    fun updateData(data: CompanySite): Flow<CompanySite?> {
        return db.uploadProcess({
            return@uploadProcess hashMapOf(
                "id" to data.id,
                "name" to data.name,
                "shiftList" to data.shiftsIdList
            )
        }, data, companyInfoPath, data.id)
    }
}
