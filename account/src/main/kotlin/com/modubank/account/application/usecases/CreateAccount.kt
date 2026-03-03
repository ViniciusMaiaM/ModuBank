package com.modubank.account.application.usecases

import com.modubank.account.application.repositories.AccountRepository
import com.modubank.account.application.repositories.UserRepository
import com.modubank.account.domain.Account
import com.modubank.account.domain.SupportedCurrency
import com.modubank.account.domain.exception.DomainException
import com.modubank.account.domain.exception.UnsupportedCurrencyException
import com.modubank.account.infrastructure.config.AccountConfig
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
    private val accountConfig: AccountConfig,
) {
    private val log = LoggerFactory.getLogger(CreateAccount::class.java)

    fun execute(cmd: CreateAccountCommand): Account {
        log.info(
            "Starting account creation userId={}, currency={}",
            cmd.userId,
            cmd.currency,
        )

        val supportedCurrency =
            SupportedCurrency.fromCode(cmd.currency)
                ?: throw UnsupportedCurrencyException(cmd.currency)

        if (userRepository.findById(cmd.userId).isEmpty) {
            throw DomainException("user_not_found")
        }

        val account =
            Account(
                userId = cmd.userId,
                currency = supportedCurrency.code,
                branchCode = accountConfig.branchCode,
                accountNumber = generateAccountNumber(cmd.userId, supportedCurrency),
            )

        val saved = accountRepository.save(account)

        log.info(
            "Account created successfully accountId={}, userId={}",
            saved.id,
            saved.userId,
        )

        return saved
    }

    private fun generateAccountNumber(
        userId: UUID,
        currency: SupportedCurrency,
    ): String {
        val base = UUID.randomUUID().toString().replace("-", "").take(10)
        val checkDigits = calculateIso7064Mod97(base)
        return "$base$checkDigits"
    }

    private fun calculateIso7064Mod97(input: String): String {
        val numericInput = input.map { it.code - 48 }.toMutableList()
        var remainder = 0
        for (digit in numericInput) {
            remainder = (remainder * 10 + digit) % 97
        }
        val checkDigits = 98 - remainder
        return checkDigits.toString().padStart(2, '0')
    }
}
