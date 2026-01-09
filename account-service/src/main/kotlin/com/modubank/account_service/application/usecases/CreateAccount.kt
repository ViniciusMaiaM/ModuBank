package com.modubank.account_service.application.usecases

import com.modubank.account_service.application.repositories.AccountRepository
import com.modubank.account_service.domain.Account
import org.springframework.stereotype.Service
import java.util.UUID

data class CreateAccountCommand(val userId: UUID, val currency: String)

@Service
class CreateAccount(private val repository: AccountRepository) {

    fun execute(cmd: CreateAccountCommand): Account {
        val accountNumber = generateAccountNumber(cmd.userId)
        val account = Account(
            userId = cmd.userId,
            currency = cmd.currency,
            accountNumber = accountNumber
        )
        return repository.save(account)
    }

    private fun generateAccountNumber(seed: UUID): String {
        val digits = seed.toString().replace("-", "").takeLast(12)
        val base = if (digits.isNotEmpty()) digits else (100000000000..999999999999).random().toString()
        val checksum = base.sumOf { it.code } % 9
        return "$base-$checksum"
    }
}
