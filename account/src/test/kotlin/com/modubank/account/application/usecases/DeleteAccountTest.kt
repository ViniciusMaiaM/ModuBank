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

class DeleteAccountTest {
    private val accountRepository: AccountRepository = mockk()
    private val deleteAccount = DeleteAccount(accountRepository)

    @Test
    fun `should delete account successfully`() {
        // Given
        val accountId = UUID.randomUUID()
        val account =
            Account(
                id = accountId,
                userId = UUID.randomUUID(),
                currency = "BRL",
                status = AccountStatus.BLOCKED,
                accountNumber = "1234567890-12",
                branchCode = "0001",
                type = AccountType.CHECKING,
                createdAt = OffsetDateTime.now().minusDays(1),
                updatedAt = OffsetDateTime.now().minusDays(1),
            )

        every { accountRepository.findById(accountId) } returns Optional.of(account)
        every { accountRepository.delete(account) } returns Unit

        // When
        deleteAccount.execute(accountId)

        // Then
        verify { accountRepository.findById(accountId) }
        verify { accountRepository.delete(account) }
    }

    @Test
    fun `should throw exception when account is active`() {
        // Given
        val accountId = UUID.randomUUID()
        val account =
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

        every { accountRepository.findById(accountId) } returns Optional.of(account)

        // When & Then
        val exception =
            assertThrows<IllegalStateException> {
                deleteAccount.execute(accountId)
            }

        assertEquals("Cannot delete active account. Please block or close the account first.", exception.message)
        verify { accountRepository.findById(accountId) }
    }

    @Test
    fun `should throw exception when account not found`() {
        // Given
        val accountId = UUID.randomUUID()
        every { accountRepository.findById(accountId) } returns Optional.empty()

        // When & Then
        assertThrows<com.modubank.account.domain.exception.AccountNotFoundException> {
            deleteAccount.execute(accountId)
        }

        verify { accountRepository.findById(accountId) }
    }
}
