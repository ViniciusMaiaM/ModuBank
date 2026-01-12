package com.modubank.account.application.usecases

import com.modubank.account.application.repositories.AccountRepository
import com.modubank.account.domain.Account
import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class GetUserAccountsTest {
    private lateinit var accountRepository: AccountRepository
    private lateinit var getAccount: GetAccount

    @BeforeEach
    fun setup() {
        accountRepository = mockk()
        getAccount = GetAccount(accountRepository)
    }

    @Test
    fun `should return account when found`() {
        val account =
            Account(
                id = UUID.randomUUID(),
                userId = UUID.randomUUID(),
                currency = "BRL",
                accountNumber = "123",
                branchCode = "0001",
            )

        every { accountRepository.findById(account.id) } returns Optional.of(account)

        val result = getAccount.byId(account.id)

        assertTrue(result.isPresent)
        assertEquals(account.id, result.get().id)
    }

    @Test
    fun `should return empty when account not found`() {
        val id = UUID.randomUUID()
        every { accountRepository.findById(id) } returns Optional.empty()

        val result = getAccount.byId(id)

        assertTrue(result.isEmpty)
    }
}
