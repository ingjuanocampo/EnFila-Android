package com.ingjuanocampo.enfila.domain.data

import com.ingjuanocampo.enfila.domain.data.source.LocalSource
import com.ingjuanocampo.enfila.domain.data.source.RemoteSource
import com.ingjuanocampo.enfila.domain.entity.Client
import com.ingjuanocampo.enfila.domain.usecases.repository.ClientRepository


class ClientRepositoryImpl(
    remoteSource: RemoteSource<Client>,
    localSource: LocalSource<Client>):
    RepositoryImp<Client>(remoteSource, localSource), ClientRepository {
}