package com.modubank.account.integration

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
class RegisterUserIT {
    @Autowired
    lateinit var registerUser: RegisterUser

    @Test
    fun `should register user and create an account`() {
        // Arrange
        val command =
            RegisterUserCommand(
                firstName = "Vinicius",
                lastName = "Maia",
                email = "vinicius@modubank.com",
                password = "123456",
                cpf = "12345678900",
                birthDate = LocalDate.of(2002, 1, 1),
                phone = "84999999999",
                street = "Rua A",
                number = "123",
                complement = null,
                neighborhood = "Centro",
                city = "Natal",
                state = "RN",
                zipCode = "59000-000",
            )

        // Act
        val (user, account) = registerUser.execute(command)

        // Assert
        assertThat(user.id).isNotNull
        assertThat(account.id).isNotNull
        assertThat(account.userId).isEqualTo(user.id)
        assertThat(account.currency).isEqualTo("BRL")
    }
}
