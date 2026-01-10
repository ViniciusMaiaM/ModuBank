package com.modubank.account.infrastructure.persistence.jpa

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface SpringDataUserRepository : JpaRepository<UserEntity, UUID> {
    fun existsByEmail(email: String): Boolean

    fun existsByCpf(cpf: String): Boolean
}
