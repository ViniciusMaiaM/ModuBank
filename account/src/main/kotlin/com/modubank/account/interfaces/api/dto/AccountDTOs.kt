package com.modubank.account.interfaces.api.dto

import com.modubank.account.domain.Account
import com.modubank.account.domain.AccountStatus
import jakarta.validation.constraints.NotBlank
import java.time.OffsetDateTime
import java.util.*

data class CreateAccountRequest(
    val userId: UUID,
    @field:NotBlank val currency: String,
)

data class AccountResponse(
    val id: UUID,
    val userId: UUID,
    val currency: String,
    val status: AccountStatus,
    val createdAt: OffsetDateTime,
)

fun Account.toResponse() = AccountResponse(id, userId, currency, status, createdAt)
