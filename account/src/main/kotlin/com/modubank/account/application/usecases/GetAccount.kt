package com.modubank.account.application.usecases

import com.modubank.account.application.repositories.AccountRepository
import com.modubank.account.domain.Account
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*

@Service
class GetAccount(
    private val repository: AccountRepository,
) {
    private val log = LoggerFactory.getLogger(GetAccount::class.java)

    fun byId(id: UUID): Optional<Account> {
        log.info("Fetching account by id={}", id)

        val account = repository.findById(id)

        if (account.isEmpty) {
            log.warn("Account not found accountId={}", id)
        } else {
            log.info(
                "Account retrieved successfully accountId={}, userId={}",
                account.get().id,
                account.get().userId,
            )
        }

        return account
    }
}
