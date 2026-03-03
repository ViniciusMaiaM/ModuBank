package com.modubank.account.interfaces.api.dto

import com.modubank.account.domain.AccountType

data class UpdateAccountRequest(
    val accountType: AccountType?,
    val currency: String?,
)
