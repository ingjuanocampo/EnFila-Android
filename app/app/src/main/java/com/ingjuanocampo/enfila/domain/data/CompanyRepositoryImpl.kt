package com.ingjuanocampo.enfila.domain.data

import com.ingjuanocampo.enfila.domain.data.source.companysite.CompanySiteLocalSource
import com.ingjuanocampo.enfila.domain.data.source.companysite.CompanySiteRemoteSource
import com.ingjuanocampo.enfila.domain.entity.CompanySite
import com.ingjuanocampo.enfila.domain.usecases.repository.CompanyRepository

class CompanyRepositoryImpl(val remoteSource: CompanySiteRemoteSource, val localSource: CompanySiteLocalSource):
    CompanyRepository,
    RepositoryImp<CompanySite>(remoteSource, localSource) {
}
