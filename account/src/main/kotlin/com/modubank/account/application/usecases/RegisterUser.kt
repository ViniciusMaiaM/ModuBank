package com.modubank.account.application.usecases

import com.modubank.account.application.repositories.AccountRepository
import com.modubank.account.application.repositories.UserRepository
import com.modubank.account.domain.Account
import com.modubank.account.domain.AccountType
import com.modubank.account.domain.User
import org.slf4j.LoggerFactory
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
    private val log = LoggerFactory.getLogger(RegisterUser::class.java)

    fun execute(cmd: RegisterUserCommand): Pair<User, Account> {
        log.info(
            "Starting user registration email={}, cpf={}",
            cmd.email,
            cmd.cpf,
        )

        if (userRepo.existsByEmail(cmd.email)) {
            log.warn("Email already in use email={}", maskEmail(cmd.email))
            throw IllegalArgumentException("email_already_in_use")
        }

        if (userRepo.existsByCpf(cmd.cpf)) {
            log.warn("CPF already in use cpf={}", cmd.cpf)
            throw IllegalArgumentException("cpf_already_in_use")
        }

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

        log.info("User created successfully userId={}", user.id)

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

        log.info(
            "Account created for user userId={}, accountId={}",
            user.id,
            account.id,
        )

        return user to account
    }

    private fun maskEmail(email: String): String {
        val atIndex = email.indexOf("@")
        return if (atIndex > 1) {
            "***" + email.substring(atIndex)
        } else {
            "***"
        }
    }

    private fun generateAccountNumber(seed: UUID): String {
        val digits = seed.toString().replace("-", "").takeLast(12)
        val base = digits.ifEmpty { (100000000000..999999999999).random().toString() }
        val checksum = base.map { it.code }.sum() % 9
        return "$base-$checksum"
    }
}
