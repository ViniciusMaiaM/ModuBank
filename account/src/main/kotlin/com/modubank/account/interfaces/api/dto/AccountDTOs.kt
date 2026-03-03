package com.modubank.account.interfaces.api.dto

import com.modubank.account.domain.Account
import com.modubank.account.domain.AccountStatus
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import java.time.OffsetDateTime
import java.util.UUID

@Schema(description = "Request to create a new account")
data class CreateAccountRequest(
    @field:Schema(description = "User UUID that will own the account", example = "550e8400-e29b-41d4-a716-446655440000")
    val userId: UUID,
    @field:NotBlank
    @field:Schema(description = "Currency code ISO 4217", example = "BRL", allowableValues = ["BRL", "USD", "EUR", "GBP", "JPY"])
    val currency: String,
)

@Schema(description = "Account response data")
data class AccountResponse(
    @field:Schema(description = "Account UUID", example = "550e8400-e29b-41d4-a716-446655440000")
    val id: UUID,
    @field:Schema(description = "Owner user UUID", example = "550e8400-e29b-41d4-a716-446655440000")
    val userId: UUID,
    @field:Schema(description = "Currency code ISO 4217", example = "BRL")
    val currency: String,
    @field:Schema(description = "Account status", example = "ACTIVE")
    val status: AccountStatus,
    @field:Schema(description = "Account creation timestamp")
    val createdAt: OffsetDateTime,
)

fun Account.toResponse() = AccountResponse(id, userId, currency, status, createdAt)
