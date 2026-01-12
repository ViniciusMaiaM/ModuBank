package com.modubank.account.application.usecases

import com.modubank.account.application.repositories.AccountRepository
import com.modubank.account.application.repositories.UserRepository
import com.modubank.account.domain.Account
import com.modubank.account.domain.DomainException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.UUID

data class CreateAccountCommand(
    val userId: UUID,
    val currency: String,
)

@Service
class CreateAccount(
    private val accountRepository: AccountRepository,
    private val userRepository: UserRepository,
) {
    private val log = LoggerFactory.getLogger(CreateAccount::class.java)

    fun execute(cmd: CreateAccountCommand): Account {
        log.info(
            "Starting account creation userId={}, currency={}",
            cmd.userId,
            cmd.currency,
        )

        if (cmd.currency.isBlank()) {
            log.warn("Invalid currency userId={}", cmd.userId)
            throw IllegalArgumentException("currency_must_not_be_blank")
        }

        if (userRepository.findById(cmd.userId).isEmpty) {
            throw DomainException("user_not_found")
        }

        val account =
            Account(
                userId = cmd.userId,
                currency = cmd.currency,
                branchCode = DEFAULT_BRANCH,
                accountNumber = generateAccountNumber(cmd.userId),
            )

        val saved = accountRepository.save(account)

        log.info(
            "Account created successfully accountId={}, userId={}",
            saved.id,
            saved.userId,
        )

        return saved
    }

    private fun generateAccountNumber(userId: UUID): String {
        val base =
            userId
                .toString()
                .replace("-", "")
                .takeLast(12)

        val checksum = base.sumOf { it.code } % 9
        return "$base-$checksum"
    }

    companion object {
        private const val DEFAULT_BRANCH = "0001"
    }
}
