package com.modubank.account_service.infrastructure.persistence.jpa

import com.modubank.account_service.infrastructure.persistence.jpa.UserEntity
import com.modubank.account_service.application.repositories.UserRepository
import com.modubank.account_service.domain.User
import com.modubank.account_service.domain.UserStatus
import org.springframework.stereotype.Component
import java.util.*

@Component
class UserRepositoryJpaAdapter(private val jpa: SpringDataUserRepository) : UserRepository {
    override fun save(user: User): User {
        val e =
            UserEntity(
                id = user.id,
                firstName = user.firstName,
                lastName = user.lastName,
                email = user.email,
                passwordHash = user.passwordHash,
                cpf = user.cpf,
                birthDate = user.birthDate,
                phone = user.phone,
                street = user.street,
                number = user.number,
                complement = user.complement,
                neighborhood = user.neighborhood,
                city = user.city,
                state = user.state,
                zipCode = user.zipCode,
                status =
                when (user.status) {
                    UserStatus.ACTIVE -> UserEntity.UserStatus.ACTIVE
                    UserStatus.BLOCKED -> UserEntity.UserStatus.BLOCKED
                },
                createdAt = user.createdAt,
                updatedAt = user.updatedAt,
            )
        jpa.save(e)
        return user
    }

    override fun findById(id: UUID): Optional<User> = jpa.findById(id).map { toDomain(it) }

    override fun existsByEmail(email: String) = jpa.existsByEmail(email)

    override fun existsByCpf(cpf: String) = jpa.existsByCpf(cpf)

    private fun toDomain(e: UserEntity) =
        User(
            id = e.id!!,
            firstName = e.firstName!!,
            lastName = e.lastName!!,
            email = e.email!!,
            passwordHash = e.passwordHash!!,
            cpf = e.cpf!!,
            birthDate = e.birthDate!!,
            phone = e.phone!!,
            street = e.street!!,
            number = e.number!!,
            complement = e.complement,
            neighborhood = e.neighborhood!!,
            city = e.city!!,
            state = e.state!!,
            zipCode = e.zipCode!!,
            status =
            when (e.status!!) {
                UserEntity.UserStatus.ACTIVE -> UserStatus.ACTIVE
                UserEntity.UserStatus.BLOCKED -> UserStatus.BLOCKED
            },
            createdAt = e.createdAt!!,
            updatedAt = e.updatedAt!!,
        )
}
