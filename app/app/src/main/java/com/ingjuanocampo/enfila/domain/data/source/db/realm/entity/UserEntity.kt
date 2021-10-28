package com.ingjuanocampo.enfila.domain.data.source.db.realm.entity

import com.ingjuanocampo.enfila.domain.entity.User
import com.ingjuanocampo.enfila.domain.util.EMPTY_STRING
import io.realm.RealmObject

class UserEntity: RealmObject {

    var id: String = EMPTY_STRING
    var phone: String = EMPTY_STRING
    var name: String = EMPTY_STRING
    var companyIds: String = EMPTY_STRING
}

fun User.toEntity(): UserEntity {
    val here = this
    return UserEntity().apply {
        id = here.id
        phone = here.phone
        name = here.name ?: EMPTY_STRING
        companyIds = here.companyIds.toEntity()
    }
}

fun UserEntity.toModel() : User{
    val here = this
    return User(
        id = here.id!!,
        phone = here.phone!!,
        name = here.name,
        companyIds = here.companyIds.toList())
}

fun List<String>?.toEntity(): String {
    return this?.joinToString(",") ?: EMPTY_STRING
}

fun String.toList(): List<String> {
    return this.split(",")
}