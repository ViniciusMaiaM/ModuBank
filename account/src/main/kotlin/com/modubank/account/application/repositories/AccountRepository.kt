package com.modubank.account.application.repositories

import com.modubank.account.domain.Account
import java.util.Optional
import java.util.UUID

interface AccountRepository {
    fun save(account: Account): Account

    fun findById(id: UUID): Optional<Account>

    fun findByUserId(userId: UUID): List<Account>
}
