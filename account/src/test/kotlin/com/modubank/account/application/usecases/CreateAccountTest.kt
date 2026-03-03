package com.modubank.account.application.usecases

import com.modubank.account.application.repositories.AccountRepository
import com.modubank.account.application.repositories.UserRepository
import com.modubank.account.domain.Account
import com.modubank.account.domain.User
import com.modubank.account.domain.exception.DomainException
import com.modubank.account.domain.exception.UnsupportedCurrencyException
import com.modubank.account.infrastructure.config.AccountConfig
import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class CreateAccountTest {
    private lateinit var accountRepository: AccountRepository
    private lateinit var userRepository: UserRepository
    private lateinit var accountConfig: AccountConfig
    private lateinit var createAccount: CreateAccount

    @BeforeEach
    fun setup() {
        accountRepository = mockk()
        userRepository = mockk()
        accountConfig = AccountConfig()
        accountConfig.branchCode = "0001"
        createAccount = CreateAccount(accountRepository, userRepository, accountConfig)
    }

    @Test
    fun `should create account successfully`() {
        val userId = UUID.randomUUID()
        val user = mockk<User>()

        every { userRepository.findById(userId) } returns Optional.of(user)
        every { accountRepository.save(any<Account>()) } answers { firstArg() }

        val result =
            createAccount.execute(
                CreateAccountCommand(
                    userId = userId,
                    currency = "BRL",
                ),
            )

        assertEquals(userId, result.userId)
        assertEquals("BRL", result.currency)
        assertEquals("0001", result.branchCode)
        assertTrue(result.accountNumber.isNotBlank())
        verify(exactly = 1) { accountRepository.save(any()) }
    }

    @Test
    fun `should throw exception when user not found`() {
        val userId = UUID.randomUUID()

        every { userRepository.findById(userId) } returns Optional.empty()

        val exception =
            assertThrows(DomainException::class.java) {
                createAccount.execute(
                    CreateAccountCommand(
                        userId = userId,
                        currency = "BRL",
                    ),
                )
            }

        assertEquals("user_not_found", exception.code)
    }

    @Test
    fun `should throw exception for unsupported currency`() {
        val userId = UUID.randomUUID()

        val exception =
            assertThrows(UnsupportedCurrencyException::class.java) {
                createAccount.execute(
                    CreateAccountCommand(
                        userId = userId,
                        currency = "INVALID",
                    ),
                )
            }

        assertNotNull(exception.message)
    }

    @Test
    fun `should create account with USD currency`() {
        val userId = UUID.randomUUID()
        val user = mockk<User>()

        every { userRepository.findById(userId) } returns Optional.of(user)
        every { accountRepository.save(any<Account>()) } answers { firstArg() }

        val result =
            createAccount.execute(
                CreateAccountCommand(
                    userId = userId,
                    currency = "USD",
                ),
            )

        assertEquals("USD", result.currency)
    }

    @Test
    fun `should create account with EUR currency`() {
        val userId = UUID.randomUUID()
        val user = mockk<User>()

        every { userRepository.findById(userId) } returns Optional.of(user)
        every { accountRepository.save(any<Account>()) } answers { firstArg() }

        val result =
            createAccount.execute(
                CreateAccountCommand(
                    userId = userId,
                    currency = "EUR",
                ),
            )

        assertEquals("EUR", result.currency)
    }
}
