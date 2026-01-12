package com.modubank.account.integration

import com.modubank.account.application.usecases.GetAccount
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
class GetAccountIT {
    @Autowired
    lateinit var registerUser: RegisterUser

    @Autowired
    lateinit var getAccount: GetAccount

    @Test
    fun `should retrieve account by id`() {
        // Arrange
        val (_, account) =
            registerUser.execute(
                RegisterUserCommand(
                    firstName = "Vinicius",
                    lastName = "Maia",
                    email = "vinicius3@modubank.com",
                    password = "123456",
                    cpf = "11122233344",
                    birthDate = LocalDate.of(2002, 1, 1),
                    phone = "84999999999",
                    street = "Rua C",
                    number = "789",
                    complement = null,
                    neighborhood = "Centro",
                    city = "Natal",
                    state = "RN",
                    zipCode = "59000-000",
                ),
            )

        // Act
        val result = getAccount.byId(account.id)

        // Assert
        assertThat(result).isPresent
        assertThat(result.get().id).isEqualTo(account.id)
    }
}
