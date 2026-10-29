package com.ingjuanocampo.enfila.domain.usecases

import com.ingjuanocampo.enfila.data.backend.source.BackendTipSource
import com.ingjuanocampo.enfila.domain.entity.Tip
import com.ingjuanocampo.enfila.domain.usecases.repository.UserRepository
import javax.inject.Inject

class LoadTipsUC @Inject constructor(
    private val backendTipSource: BackendTipSource,
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(): List<Tip> {
        val userId = userRepository.id
        if (userId.isBlank()) return emptyList()
        return backendTipSource.getTipsForUser(userId) ?: emptyList()
    }
}
