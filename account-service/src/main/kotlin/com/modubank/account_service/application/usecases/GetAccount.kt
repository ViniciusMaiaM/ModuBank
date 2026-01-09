package com.modubank.account_service.application.usecases

import com.modubank.account_service.application.repositories.AccountRepository
import com.modubank.account_service.domain.Account
import org.springframework.stereotype.Service
import java.util.*

@Service
class GetAccount(private val repository: AccountRepository) {
    fun byId(id: UUID): Optional<Account> = repository.findById(id)
}
