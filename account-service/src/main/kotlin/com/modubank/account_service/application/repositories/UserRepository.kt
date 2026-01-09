package com.modubank.account_service.application.repositories

import com.modubank.account_service.domain.User
import java.util.*

interface UserRepository {
    fun save(user: User): User

    fun findById(id: UUID): Optional<User>

    fun existsByEmail(email: String): Boolean

    fun existsByCpf(cpf: String): Boolean
}
