package com.modubank.account.interfaces.api.dto

import com.modubank.account.domain.Account
import com.modubank.account.domain.AccountType
import com.modubank.account.domain.User
import com.modubank.account.domain.UserStatus
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.*

data class RegisterUserRequest(
    @field:NotBlank val firstName: String,
    @field:NotBlank val lastName: String,
    @field:Email val email: String,
    @field:NotBlank val password: String,
    @field:Pattern(regexp = "\\d{11}") val cpf: String,
    val birthDate: LocalDate,
    @field:NotBlank val phone: String,
    @field:NotBlank val street: String,
    @field:NotBlank val number: String,
    val complement: String? = null,
    @field:NotBlank val neighborhood: String,
    @field:NotBlank val city: String,
    @field:Pattern(regexp = "^[A-Z]{2}$") val state: String,
    @field:NotBlank val zipCode: String,
    val currency: String = "BRL",
    val accountType: AccountType = AccountType.CHECKING,
)

data class UserResponse(
    val id: UUID,
    val firstName: String,
    val lastName: String,
    val email: String,
    val cpf: String,
    val birthDate: LocalDate,
    val phone: String,
    val street: String,
    val number: String,
    val complement: String?,
    val neighborhood: String,
    val city: String,
    val state: String,
    val zipCode: String,
    val status: UserStatus,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
)

data class AccountSummaryResponse(
    val id: UUID,
    val accountNumber: String,
    val branchCode: String,
    val type: AccountType,
    val currency: String,
    val createdAt: OffsetDateTime,
)

data class RegisterUserResponse(val user: UserResponse, val account: AccountSummaryResponse)

fun User.toResponse() =
    UserResponse(
        id,
        firstName,
        lastName,
        email,
        cpf,
        birthDate,
        phone,
        street,
        number,
        complement,
        neighborhood,
        city,
        state,
        zipCode,
        status,
        createdAt,
        updatedAt,
    )

fun Account.toSummary() = AccountSummaryResponse(id, accountNumber, branchCode, type, currency, createdAt)
