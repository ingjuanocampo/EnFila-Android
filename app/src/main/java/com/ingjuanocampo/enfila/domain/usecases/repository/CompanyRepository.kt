package com.ingjuanocampo.enfila.domain.usecases.repository

import com.ingjuanocampo.enfila.domain.entity.CompanySite
import com.ingjuanocampo.enfila.domain.usecases.repository.base.Repository

interface CompanyRepository : Repository<CompanySite> {
    
    suspend fun createCompanySite(companySite: CompanySite): CompanySite?
    
}
