package com.modubank.account.application.usecases

import com.modubank.account.application.repositories.AccountRepository
import com.modubank.account.domain.AccountStatus
import com.modubank.account.domain.exception.AccountNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class DeleteAccount(
    private val accountRepository: AccountRepository,
) {
    private val log = LoggerFactory.getLogger(DeleteAccount::class.java)

    @Transactional
    fun execute(accountId: UUID) {
        log.info("Deleting account accountId={}", accountId)

        val account =
            accountRepository.findById(accountId)
                .orElseThrow { AccountNotFoundException(accountId) }

        if (account.status == AccountStatus.ACTIVE) {
            throw IllegalStateException("Cannot delete active account. Please block or close the account first.")
        }

        accountRepository.delete(account)
        log.info("Account deleted successfully accountId={}", accountId)
    }
}
