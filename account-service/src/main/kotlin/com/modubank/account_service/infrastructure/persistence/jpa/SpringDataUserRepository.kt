package com.modubank.account_service.infrastructure.persistence.jpa

import com.modubank.account_service.infrastructure.persistence.jpa.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface SpringDataUserRepository : JpaRepository<UserEntity, UUID> {
    fun existsByEmail(email: String): Boolean

    fun existsByCpf(cpf: String): Boolean
}
