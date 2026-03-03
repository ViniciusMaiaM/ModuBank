package com.modubank.account.application.usecases

import com.modubank.account.application.repositories.UserRepository
import com.modubank.account.domain.User
import com.modubank.account.domain.exception.EmailAlreadyInUseException
import com.modubank.account.domain.exception.UserNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.OffsetDateTime
import java.util.UUID

data class UpdateUserCommand(
    val id: UUID,
    val firstName: String?,
    val lastName: String?,
    val email: String?,
    val phone: String?,
    val street: String?,
    val number: String?,
    val complement: String?,
    val neighborhood: String?,
    val city: String?,
    val state: String?,
    val zipCode: String?,
)

@Service
class UpdateUser(
    private val userRepository: UserRepository,
) {
    private val log = LoggerFactory.getLogger(UpdateUser::class.java)

    @Transactional
    fun execute(cmd: UpdateUserCommand): User {
        log.info("Updating user userId={}", cmd.id)

        val user =
            userRepository.findById(cmd.id)
                .orElseThrow { UserNotFoundException(cmd.id) }

        val newEmail = cmd.email ?: user.email
        if (newEmail != user.email) {
            userRepository.findByEmail(newEmail).ifPresent { existing ->
                if (existing.id != user.id) {
                    throw EmailAlreadyInUseException()
                }
            }
        }

        val updatedUser =
            user.copy(
                firstName = cmd.firstName ?: user.firstName,
                lastName = cmd.lastName ?: user.lastName,
                email = newEmail,
                phone = cmd.phone ?: user.phone,
                street = cmd.street ?: user.street,
                number = cmd.number ?: user.number,
                complement = cmd.complement ?: user.complement,
                neighborhood = cmd.neighborhood ?: user.neighborhood,
                city = cmd.city ?: user.city,
                state = cmd.state ?: user.state,
                zipCode = cmd.zipCode ?: user.zipCode,
                updatedAt = OffsetDateTime.now(),
            )

        val saved = userRepository.save(updatedUser)
        log.info("User updated successfully userId={}", saved.id)
        return saved
    }
}
