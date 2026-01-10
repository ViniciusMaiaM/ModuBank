package com.modubank.account.integration

import com.modubank.account.application.usecases.CreateAccount
import com.modubank.account.application.usecases.CreateAccountCommand
import com.modubank.account.application.usecases.RegisterUser
import com.modubank.account.application.usecases.RegisterUserCommand
import com.modubank.account.integration.config.IntegrationTestConfig
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@SpringBootTest
@Import(IntegrationTestConfig::class)
@Transactional
class CreateAccountIT {
    @Autowired
    lateinit var registerUser: RegisterUser

    @Autowired
    lateinit var createAccount: CreateAccount

    @Test
    fun `should create a new account for existing user`() {
        // Arrange – cria usuário
        val (user, _) =
            registerUser.execute(
                RegisterUserCommand(
                    firstName = "Vinicius",
                    lastName = "Maia",
                    email = "vinicius2@modubank.com",
                    password = "123456",
                    cpf = "98765432100",
                    birthDate = LocalDate.of(2002, 1, 1),
                    phone = "84999999999",
                    street = "Rua B",
                    number = "456",
                    complement = null,
                    neighborhood = "Centro",
                    city = "Natal",
                    state = "RN",
                    zipCode = "59000-000",
                ),
            )

        // Act
        val account =
            createAccount.execute(
                CreateAccountCommand(
                    userId = user.id,
                    currency = "USD",
                ),
            )

        // Assert
        assertThat(account.id).isNotNull
        assertThat(account.userId).isEqualTo(user.id)
        assertThat(account.currency).isEqualTo("USD")
        assertThat(account.accountNumber).isNotBlank
    }
}
