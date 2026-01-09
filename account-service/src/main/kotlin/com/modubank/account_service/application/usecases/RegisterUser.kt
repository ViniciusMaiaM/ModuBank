package com.modubank.account_service.application.usecases

import com.modubank.account_service.application.repositories.AccountRepository
import com.modubank.account_service.application.repositories.UserRepository
import com.modubank.account_service.domain.Account
import com.modubank.account_service.domain.AccountType
import com.modubank.account_service.domain.User
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.UUID

data class RegisterUserCommand(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val cpf: String,
    val birthDate: LocalDate,
    val phone: String,
    val street: String,
    val number: String,
    val complement: String?,
    val neighborhood: String,
    val city: String,
    val state: String,
    val zipCode: String,
    val currency: String = "BRL",
    val accountType: AccountType = AccountType.CHECKING,
)

@Service
class RegisterUser(
    private val userRepo: UserRepository,
    private val accountRepo: AccountRepository,
) {
    fun execute(cmd: RegisterUserCommand): Pair<User, Account> {
        if (userRepo.existsByEmail(cmd.email)) {
            throw IllegalArgumentException("email_already_in_use")
        }
        if (userRepo.existsByCpf(cmd.cpf)) throw IllegalArgumentException("cpf_already_in_use")

        val passwordHash = BCrypt.hashpw(cmd.password, BCrypt.gensalt(10))
        val user =
            userRepo.save(
                User(
                    firstName = cmd.firstName,
                    lastName = cmd.lastName,
                    email = cmd.email,
                    passwordHash = passwordHash,
                    cpf = cmd.cpf,
                    birthDate = cmd.birthDate,
                    phone = cmd.phone,
                    street = cmd.street,
                    number = cmd.number,
                    complement = cmd.complement,
                    neighborhood = cmd.neighborhood,
                    city = cmd.city,
                    state = cmd.state,
                    zipCode = cmd.zipCode,
                ),
            )

        val accountNumber = generateAccountNumber(user.id)
        val account =
            accountRepo.save(
                Account(
                    userId = user.id,
                    currency = cmd.currency,
                    accountNumber = accountNumber,
                    branchCode = "0001",
                    type = cmd.accountType,
                ),
            )
        return user to account
    }

    private fun generateAccountNumber(seed: UUID): String {
        // Exemplo simples: últimos 12 dígitos do UUID numérico + dígito
        val digits = seed.toString().replace("-", "").takeLast(12)
        val base = digits.ifEmpty { (100000000000..999999999999).random().toString() }
        val checksum = base.map { it.code }.sum() % 9
        return "$base-$checksum"
    }
}
