package com.modubank.account.application.usecases

import com.modubank.account.application.repositories.UserRepository
import com.modubank.account.domain.User
import com.modubank.account.domain.UserStatus
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.OffsetDateTime
import java.util.*

class UpdateUserTest {
    private val userRepository: UserRepository = mockk()
    private val updateUser = UpdateUser(userRepository)

    @Test
    fun `should update user successfully`() {
        // Given
        val userId = UUID.randomUUID()
        val originalUser =
            User(
                id = userId,
                firstName = "John",
                lastName = "Doe",
                email = "john@example.com",
                cpf = "12345678901",
                birthDate = java.time.LocalDate.of(1990, 1, 1),
                phone = "11999999999",
                street = "Rua Test",
                number = "123",
                complement = null,
                neighborhood = "Centro",
                city = "São Paulo",
                state = "SP",
                zipCode = "01310-100",
                status = UserStatus.ACTIVE,
                createdAt = OffsetDateTime.now().minusDays(1),
                updatedAt = OffsetDateTime.now().minusDays(1),
                passwordHash = "hashed_password",
            )

        val command =
            UpdateUserCommand(
                id = userId,
                firstName = "John Updated",
                lastName = "Doe Updated",
                email = "john.updated@example.com",
                phone = "11888888888",
                street = "Rua Updated",
                number = "456",
                complement = "Apt 789",
                neighborhood = "Updated",
                city = "Updated City",
                state = "RJ",
                zipCode = "20000-000",
            )

        every { userRepository.findById(userId) } returns Optional.of(originalUser)
        every { userRepository.findByEmail("john.updated@example.com") } returns Optional.empty()
        every { userRepository.save(any()) } answers { firstArg() }

        // When
        val result = updateUser.execute(command)

        // Then
        verify { userRepository.findById(userId) }
        verify {
            userRepository.save(
                match { user ->
                    user.firstName == "John Updated" &&
                        user.lastName == "Doe Updated" &&
                        user.email == "john.updated@example.com" &&
                        user.phone == "11888888888" &&
                        user.street == "Rua Updated" &&
                        user.number == "456" &&
                        user.complement == "Apt 789" &&
                        user.neighborhood == "Updated" &&
                        user.city == "Updated City" &&
                        user.state == "RJ" &&
                        user.zipCode == "20000-000"
                },
            )
        }

        assertEquals("John Updated", result.firstName)
        assertEquals("Doe Updated", result.lastName)
        assertEquals("john.updated@example.com", result.email)
    }

    @Test
    fun `should update only provided fields`() {
        // Given
        val userId = UUID.randomUUID()
        val originalUser =
            User(
                id = userId,
                firstName = "John",
                lastName = "Doe",
                email = "john@example.com",
                cpf = "12345678901",
                birthDate = java.time.LocalDate.of(1990, 1, 1),
                phone = "11999999999",
                street = "Rua Test",
                number = "123",
                complement = null,
                neighborhood = "Centro",
                city = "São Paulo",
                state = "SP",
                zipCode = "01310-100",
                status = UserStatus.ACTIVE,
                createdAt = OffsetDateTime.now().minusDays(1),
                updatedAt = OffsetDateTime.now().minusDays(1),
                passwordHash = "hashed_password",
            )

        val command =
            UpdateUserCommand(
                id = userId,
                firstName = "John Updated",
                lastName = null,
                email = null,
                phone = null,
                street = null,
                number = null,
                complement = null,
                neighborhood = null,
                city = null,
                state = null,
                zipCode = null,
            )

        every { userRepository.findById(userId) } returns Optional.of(originalUser)
        every { userRepository.save(any()) } answers { firstArg() }

        // When
        val result = updateUser.execute(command)

        // Then
        assertEquals("John Updated", result.firstName)
        assertEquals("Doe", result.lastName) // Original value preserved
        assertEquals("john@example.com", result.email) // Original value preserved
    }
}
