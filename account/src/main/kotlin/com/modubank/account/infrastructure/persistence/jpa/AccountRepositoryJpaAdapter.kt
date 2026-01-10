package com.modubank.account.infrastructure.persistence.jpa

import com.modubank.account.application.repositories.AccountRepository
import com.modubank.account.domain.Account
import com.modubank.account.domain.AccountStatus
import com.modubank.account.domain.AccountType
import org.springframework.stereotype.Component
import java.util.*

@Component
class AccountRepositoryJpaAdapter(private val jpa: SpringDataAccountRepository) :
    AccountRepository {
    override fun save(account: Account): Account {
        val e =
            AccountEntity(
                id = account.id,
                userId = account.userId,
                currency = account.currency,
                status =
                    when (account.status) {
                        AccountStatus.ACTIVE -> AccountEntity.AccountStatus.ACTIVE
                        AccountStatus.BLOCKED -> AccountEntity.AccountStatus.BLOCKED
                        AccountStatus.CLOSED -> AccountEntity.AccountStatus.CLOSED
                    },
                accountNumber = account.accountNumber,
                branchCode = account.branchCode,
                type =
                    when (account.type) {
                        AccountType.CHECKING -> AccountEntity.AccountType.CHECKING
                        AccountType.SAVINGS -> AccountEntity.AccountType.SAVINGS
                    },
                createdAt = account.createdAt,
            )
        jpa.save(e)
        return account
    }

    override fun findById(id: UUID): Optional<Account> = jpa.findById(id).map { toDomain(it) }

    override fun findByUserId(userId: UUID): List<Account> = jpa.findByUserId(userId).map { toDomain(it) }

    private fun toDomain(e: AccountEntity) =
        Account(
            id = e.id!!,
            userId = e.userId!!,
            currency = e.currency!!,
            status =
                when (e.status!!) {
                    AccountEntity.AccountStatus.ACTIVE -> AccountStatus.ACTIVE
                    AccountEntity.AccountStatus.BLOCKED -> AccountStatus.BLOCKED
                    AccountEntity.AccountStatus.CLOSED -> AccountStatus.CLOSED
                },
            accountNumber = e.accountNumber!!,
            branchCode = e.branchCode!!,
            type =
                when (e.type!!) {
                    AccountEntity.AccountType.CHECKING -> AccountType.CHECKING
                    AccountEntity.AccountType.SAVINGS -> AccountType.SAVINGS
                },
            createdAt = e.createdAt!!,
        )
}
