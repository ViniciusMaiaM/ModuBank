package com.modubank.account.application.usecases

import com.modubank.account.application.repositories.AccountRepository
import com.modubank.account.domain.Account
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.UUID

data class CreateAccountCommand(val userId: UUID, val currency: String)

@Service
class CreateAccount(private val repository: AccountRepository) {
    private val log = LoggerFactory.getLogger(RegisterUser::class.java)

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

        val account =
            Account(
                userId = cmd.userId,
                currency = cmd.currency,
                accountNumber = generateAccountNumber(cmd.userId),
            )

        val saved = repository.save(account)

        log.info(
            "Account created successfully accountId={}, userId={}",
            saved.id,
            saved.userId,
        )

        return saved
    }

    private fun generateAccountNumber(seed: UUID): String {
        val digits = seed.toString().replace("-", "").takeLast(12)
        val base = digits.ifEmpty { (100000000000..999999999999).random().toString() }
        val checksum = base.sumOf { it.code } % 9
        return "$base-$checksum"
    }
}
