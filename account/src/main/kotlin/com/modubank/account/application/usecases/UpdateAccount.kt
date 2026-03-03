package com.modubank.account.application.usecases

import com.modubank.account.application.repositories.AccountRepository
import com.modubank.account.domain.Account
import com.modubank.account.domain.AccountType
import com.modubank.account.domain.SupportedCurrency
import com.modubank.account.domain.exception.AccountNotFoundException
import com.modubank.account.domain.exception.UnsupportedCurrencyException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.OffsetDateTime
import java.util.UUID

data class UpdateAccountCommand(
    val id: UUID,
    val accountType: AccountType?,
    val currency: String?,
)

@Service
class UpdateAccount(
    private val accountRepository: AccountRepository,
) {
    private val log = LoggerFactory.getLogger(UpdateAccount::class.java)

    @Transactional
    fun execute(cmd: UpdateAccountCommand): Account {
        log.info("Updating account accountId={}", cmd.id)

        val account =
            accountRepository.findById(cmd.id)
                .orElseThrow { AccountNotFoundException(cmd.id) }

        val supportedCurrency =
            cmd.currency?.let { currency ->
                SupportedCurrency.fromCode(currency)
                    ?: throw UnsupportedCurrencyException(currency)
            }

        val updatedAccount =
            account.copy(
                type = cmd.accountType ?: account.type,
                currency = supportedCurrency?.code ?: account.currency,
                updatedAt = OffsetDateTime.now(),
            )

        val saved = accountRepository.save(updatedAccount)
        log.info("Account updated successfully accountId={}", saved.id)
        return saved
    }
}
