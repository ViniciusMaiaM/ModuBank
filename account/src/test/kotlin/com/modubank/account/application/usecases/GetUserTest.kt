package com.modubank.account.application.usecases

import com.modubank.account.application.repositories.AccountRepository
import com.modubank.account.application.repositories.UserRepository
import com.modubank.account.domain.User
import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class GetUserTest {
    private lateinit var userRepository: UserRepository
    private lateinit var accountRepository: AccountRepository
    private lateinit var createAccount: CreateAccount

    @BeforeEach
    fun setup() {
        userRepository = mockk()
        accountRepository = mockk()
        createAccount = CreateAccount(accountRepository, userRepository)
    }

    @Test
    fun `should create account for existing user`() {
        val userId = UUID.randomUUID()
        val user = mockk<User>()

        every { userRepository.findById(userId) } returns Optional.of(user)
        every { accountRepository.save(any()) } answers { firstArg() }

        val account =
            createAccount.execute(
                CreateAccountCommand(
                    userId = userId,
                    currency = "USD",
                ),
            )

        assertEquals(userId, account.userId)
        assertEquals("USD", account.currency)
    }

    @Test
    fun `should throw error when user does not exist`() {
        val userId = UUID.randomUUID()
        every { userRepository.findById(userId) } returns Optional.empty()

        assertThrows(IllegalArgumentException::class.java) {
            createAccount.execute(CreateAccountCommand(userId, "BRL"))
        }
    }
}
