package com.ingjuanocampo.enfila.domain.usecases.repository

import com.ingjuanocampo.enfila.domain.entity.Client
import com.ingjuanocampo.enfila.domain.usecases.repository.base.Repository

interface ClientRepository: Repository<List<Client>> {

}