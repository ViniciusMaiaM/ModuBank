package com.modubank.account.application.usecases

import com.modubank.account.application.repositories.AccountRepository
import com.modubank.account.application.repositories.UserRepository
import com.modubank.account.domain.Account
import com.modubank.account.domain.AccountType
import com.modubank.account.domain.CpfAlreadyInUseException
import com.modubank.account.domain.EmailAlreadyInUseException
import com.modubank.account.domain.RequiredFieldMissingException
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
        validateRequiredFields(cmd)

        if (userRepo.existsByEmail(cmd.email)) {
            throw EmailAlreadyInUseException()
        }

        if (userRepo.existsByCpf(cmd.cpf)) {
            throw CpfAlreadyInUseException()
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

        val account =
            accountRepo.save(
                Account(
                    userId = user.id,
                    currency = cmd.currency,
                    accountNumber = generateAccountNumber(user.id),
                    branchCode = "0001",
                    type = cmd.accountType,
                ),
            )

        log.info("User and account created userId={}, accountId={}", user.id, account.id)

        return user to account
    }

    private fun validateRequiredFields(cmd: RegisterUserCommand) {
        listOf(
            cmd.email to "email",
            cmd.password to "password",
            cmd.cpf to "cpf",
        ).firstOrNull { it.first.isBlank() }
            ?.let { throw RequiredFieldMissingException(it.second) }
    }

    private fun generateAccountNumber(seed: UUID): String {
        val digits = seed.toString().replace("-", "").takeLast(12)
        val checksum = digits.sumOf { it.code } % 9
        return "$digits-$checksum"
    }
}
