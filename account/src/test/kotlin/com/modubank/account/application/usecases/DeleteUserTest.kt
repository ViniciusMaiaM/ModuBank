package com.modubank.account.application.usecases

import com.modubank.account.application.repositories.AccountRepository
import com.modubank.account.application.repositories.UserRepository
import com.modubank.account.domain.Account
import com.modubank.account.domain.AccountStatus
import com.modubank.account.domain.AccountType
import com.modubank.account.domain.User
import com.modubank.account.domain.UserStatus
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.OffsetDateTime
import java.util.*

class DeleteUserTest {
    private val userRepository: UserRepository = mockk()
    private val accountRepository: AccountRepository = mockk()
    private val deleteUser = DeleteUser(userRepository, accountRepository)

    @Test
    fun `should delete user successfully`() {
        // Given
        val userId = UUID.randomUUID()
        val user =
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

        every { userRepository.findById(userId) } returns Optional.of(user)
        every { accountRepository.findByUserId(userId) } returns emptyList()
        every { userRepository.delete(user) } returns Unit

        // When
        deleteUser.execute(userId)

        // Then
        verify { userRepository.findById(userId) }
        verify { accountRepository.findByUserId(userId) }
        verify { userRepository.delete(user) }
    }

    @Test
    fun `should throw exception when user not found`() {
        // Given
        val userId = UUID.randomUUID()
        every { userRepository.findById(userId) } returns Optional.empty()

        // When & Then
        assertThrows<com.modubank.account.domain.exception.UserNotFoundException> {
            deleteUser.execute(userId)
        }

        verify { userRepository.findById(userId) }
    }

    @Test
    fun `should throw exception when user has active accounts`() {
        // Given
        val userId = UUID.randomUUID()
        val user =
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

        val account =
            Account(
                id = UUID.randomUUID(),
                userId = userId,
                currency = "BRL",
                status = AccountStatus.ACTIVE,
                accountNumber = "1234567890-12",
                branchCode = "0001",
                type = AccountType.CHECKING,
                createdAt = OffsetDateTime.now(),
            )

        every { userRepository.findById(userId) } returns Optional.of(user)
        every { accountRepository.findByUserId(userId) } returns listOf(account)

        // When & Then
        val exception =
            assertThrows<IllegalStateException> {
                deleteUser.execute(userId)
            }

        assertEquals("Cannot delete user with active accounts", exception.message)
        verify { userRepository.findById(userId) }
        verify { accountRepository.findByUserId(userId) }
    }
}
