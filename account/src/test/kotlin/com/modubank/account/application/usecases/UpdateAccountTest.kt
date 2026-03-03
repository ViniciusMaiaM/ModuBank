package com.modubank.account.application.usecases

import com.modubank.account.application.repositories.AccountRepository
import com.modubank.account.domain.Account
import com.modubank.account.domain.AccountStatus
import com.modubank.account.domain.AccountType
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.OffsetDateTime
import java.util.*

class UpdateAccountTest {
    private val accountRepository: AccountRepository = mockk()
    private val updateAccount = UpdateAccount(accountRepository)

    @Test
    fun `should update account successfully`() {
        // Given
        val accountId = UUID.randomUUID()
        val originalAccount =
            Account(
                id = accountId,
                userId = UUID.randomUUID(),
                currency = "BRL",
                status = AccountStatus.ACTIVE,
                accountNumber = "1234567890-12",
                branchCode = "0001",
                type = AccountType.CHECKING,
                createdAt = OffsetDateTime.now().minusDays(1),
                updatedAt = OffsetDateTime.now().minusDays(1),
            )

        val command =
            UpdateAccountCommand(
                id = accountId,
                accountType = AccountType.SAVINGS,
                currency = "USD",
            )

        every { accountRepository.findById(accountId) } returns Optional.of(originalAccount)
        every { accountRepository.save(any()) } answers { firstArg() }

        // When
        val result = updateAccount.execute(command)

        // Then
        verify { accountRepository.findById(accountId) }
        verify {
            accountRepository.save(
                match { account ->
                    account.type == AccountType.SAVINGS &&
                        account.currency == "USD"
                },
            )
        }

        assertEquals(AccountType.SAVINGS, result.type)
        assertEquals("USD", result.currency)
    }

    @Test
    fun `should update only provided fields`() {
        // Given
        val accountId = UUID.randomUUID()
        val originalAccount =
            Account(
                id = accountId,
                userId = UUID.randomUUID(),
                currency = "BRL",
                status = AccountStatus.ACTIVE,
                accountNumber = "1234567890-12",
                branchCode = "0001",
                type = AccountType.CHECKING,
                createdAt = OffsetDateTime.now().minusDays(1),
                updatedAt = OffsetDateTime.now().minusDays(1),
            )

        val command =
            UpdateAccountCommand(
                id = accountId,
                accountType = AccountType.SAVINGS,
                currency = null,
            )

        every { accountRepository.findById(accountId) } returns Optional.of(originalAccount)
        every { accountRepository.save(any()) } answers { firstArg() }

        // When
        val result = updateAccount.execute(command)

        // Then
        assertEquals(AccountType.SAVINGS, result.type) // Updated
        assertEquals("BRL", result.currency) // Original preserved
    }

    @Test
    fun `should throw exception for unsupported currency`() {
        // Given
        val accountId = UUID.randomUUID()
        val originalAccount =
            Account(
                id = accountId,
                userId = UUID.randomUUID(),
                currency = "BRL",
                status = AccountStatus.ACTIVE,
                accountNumber = "1234567890-12",
                branchCode = "0001",
                type = AccountType.CHECKING,
                createdAt = OffsetDateTime.now().minusDays(1),
                updatedAt = OffsetDateTime.now().minusDays(1),
            )

        val command =
            UpdateAccountCommand(
                id = accountId,
                accountType = null,
                currency = "INVALID",
            )

        every { accountRepository.findById(accountId) } returns Optional.of(originalAccount)

        // When & Then
        assertThrows<com.modubank.account.domain.exception.UnsupportedCurrencyException> {
            updateAccount.execute(command)
        }

        verify { accountRepository.findById(accountId) }
    }
}
