package com.modubank.account_service.infrastructure.persistence.jpa

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface SpringDataAccountRepository : JpaRepository<AccountEntity, UUID> {
    fun findByUserId(userId: UUID): List<AccountEntity>
}
