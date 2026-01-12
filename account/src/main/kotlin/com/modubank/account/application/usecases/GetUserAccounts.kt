package com.modubank.account.application.usecases

import com.modubank.account.application.repositories.AccountRepository
import com.modubank.account.domain.Account
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*

@Service
class GetUserAccounts(
    private val accountRepository: AccountRepository,
) {
    private val log = LoggerFactory.getLogger(GetUserAccounts::class.java)

    fun byUserId(userId: UUID): List<Account> {
        log.info("Fetching accounts for userId={}", userId)
        return accountRepository.findByUserId(userId)
    }
}
