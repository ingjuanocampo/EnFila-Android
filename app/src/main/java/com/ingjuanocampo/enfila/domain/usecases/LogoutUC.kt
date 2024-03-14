package com.ingjuanocampo.enfila.domain.usecases

import com.ingjuanocampo.enfila.domain.di.data.DELETABLE
import com.ingjuanocampo.enfila.domain.usecases.repository.base.Repository
import javax.inject.Inject

class LogoutUC
    @Inject
    constructor(
        @DELETABLE private val deletables: Set<@JvmSuppressWildcards Repository<*>>,
    ) {
        suspend operator fun invoke() {
            deletables.forEach {
                it.deleteAll()
            }
        }
    }
