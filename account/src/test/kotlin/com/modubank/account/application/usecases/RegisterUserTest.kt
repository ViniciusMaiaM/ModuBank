package com.modubank.account.application.usecases

import com.modubank.account.application.repositories.AccountRepository
import com.modubank.account.application.repositories.UserRepository
import com.modubank.account.domain.Account
import com.modubank.account.domain.AccountType
import com.modubank.account.domain.DomainValidationException
import com.modubank.account.domain.User
import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate

class RegisterUserTest {
    private lateinit var registerUser: RegisterUser
    private val userRepository = mockk<UserRepository>()
    private val accountRepository = mockk<AccountRepository>()
    private lateinit var useCase: RegisterUser

    @BeforeEach
    fun setup() {
        registerUser = RegisterUser(userRepository, accountRepository)
    }

    @Test
    fun `should register user and create account successfully`() {
        every { userRepository.existsByEmail(any()) } returns false
        every { userRepository.existsByCpf(any()) } returns false

        val cmd =
            RegisterUserCommand(
                firstName = "Vinicius",
                lastName = "Maia",
                email = "vinicius@email.com",
                password = "123456",
                cpf = "12345678900",
                birthDate = LocalDate.of(2003, 1, 1),
                phone = "84999999999",
                street = "Rua A",
                number = "10",
                complement = null,
                neighborhood = "Centro",
                city = "Natal",
                state = "RN",
                zipCode = "59000000",
                currency = "BRL",
                accountType = AccountType.CHECKING,
            )

        every { userRepository.save(any<User>()) } answers { firstArg() }
        every { accountRepository.save(any<Account>()) } answers { firstArg() }

        val (user, account) = useCase.execute(cmd)

        assertEquals(cmd.email, user.email)
        assertEquals(user.id, account.userId)
        assertEquals(AccountType.CHECKING, account.type)
        assertTrue(account.accountNumber.isNotBlank())
        verify(exactly = 1) { userRepository.save(any()) }
        verify(exactly = 1) { accountRepository.save(any()) }
    }

    @Test
    fun `should throw error when email already exists`() {
        every { userRepository.existsByEmail(any()) } returns true

        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                registerUser.execute(
                    RegisterUserCommand(
                        firstName = "A",
                        lastName = "B",
                        email = "test@test.com",
                        password = "123",
                        cpf = "123",
                        birthDate = LocalDate.now(),
                        phone = "9",
                        street = "Rua",
                        number = "1",
                        complement = null,
                        neighborhood = "Centro",
                        city = "Natal",
                        state = "RN",
                        zipCode = "59000",
                    ),
                )
            }

        assertEquals("email_already_in_use", exception.message)
    }

    @Test
    fun `should throw error when cpf already exists`() {
        every { userRepository.existsByEmail(any()) } returns false
        every { userRepository.existsByCpf(any()) } returns true

        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                registerUser.execute(
                    RegisterUserCommand(
                        firstName = "A",
                        lastName = "B",
                        email = "test@test.com",
                        password = "123",
                        cpf = "123",
                        birthDate = LocalDate.now(),
                        phone = "9",
                        street = "Rua",
                        number = "1",
                        complement = null,
                        neighborhood = "Centro",
                        city = "Natal",
                        state = "RN",
                        zipCode = "59000",
                    ),
                )
            }

        assertEquals("cpf_already_in_use", exception.message)
    }

    @Test
    fun `should throw exception when email already exists`() {
        val cmd = mockk<RegisterUserCommand>()

        every { cmd.email } returns "email@test.com"
        every { userRepository.existsByEmail(cmd.email) } returns true

        val ex =
            assertThrows(DomainValidationException::class.java) {
                useCase.execute(cmd)
            }

        assertEquals("email_already_exists", ex.code)
    }
}
