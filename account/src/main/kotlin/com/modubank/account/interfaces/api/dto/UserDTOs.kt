package com.modubank.account.interfaces.api.dto

import com.modubank.account.domain.Account
import com.modubank.account.domain.AccountType
import com.modubank.account.domain.User
import com.modubank.account.domain.UserStatus
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.UUID

@Schema(description = "Request to register a new user and create an account")
data class RegisterUserRequest(
    @field:NotBlank @field:Schema(description = "User's first name", example = "John")
    val firstName: String,
    @field:NotBlank @field:Schema(description = "User's last name", example = "Doe")
    val lastName: String,
    @field:Email @field:Schema(description = "User's email address", example = "john.doe@example.com")
    val email: String,
    @field:NotBlank @field:Schema(description = "User's password", example = "securePassword123")
    val password: String,
    @field:Pattern(regexp = "\\d{11}") @field:Schema(description = "User's CPF (11 digits)", example = "12345678901")
    val cpf: String,
    @field:Schema(description = "User's birth date", example = "1990-01-15")
    val birthDate: LocalDate,
    @field:NotBlank @field:Schema(description = "User's phone number", example = "+5511999999999")
    val phone: String,
    @field:NotBlank @field:Schema(description = "Street address", example = "Av. Paulista")
    val street: String,
    @field:NotBlank @field:Schema(description = "Street number", example = "1000")
    val number: String,
    @field:Schema(description = "Address complement (optional)", example = "Apto 501")
    val complement: String? = null,
    @field:NotBlank @field:Schema(description = "Neighborhood", example = "Bela Vista")
    val neighborhood: String,
    @field:NotBlank @field:Schema(description = "City", example = "São Paulo")
    val city: String,
    @field:Pattern(regexp = "^[A-Z]{2}$") @field:Schema(description = "State (2 letters)", example = "SP")
    val state: String,
    @field:NotBlank @field:Schema(description = "Zip code", example = "01310-100")
    val zipCode: String,
    @field:Schema(description = "Default currency for account", example = "BRL", defaultValue = "BRL")
    val currency: String = "BRL",
    @field:Schema(description = "Account type", example = "CHECKING", defaultValue = "CHECKING")
    val accountType: AccountType = AccountType.CHECKING,
)

@Schema(description = "User response data")
data class UserResponse(
    @field:Schema(description = "User UUID", example = "550e8400-e29b-41d4-a716-446655440000")
    val id: UUID,
    @field:Schema(description = "User's first name", example = "John")
    val firstName: String,
    @field:Schema(description = "User's last name", example = "Doe")
    val lastName: String,
    @field:Schema(description = "User's email", example = "john.doe@example.com")
    val email: String,
    @field:Schema(description = "User's CPF (masked)", example = "***.456.789-**")
    val cpf: String,
    @field:Schema(description = "User's birth date", example = "1990-01-15")
    val birthDate: LocalDate,
    @field:Schema(description = "User's phone", example = "+5511999999999")
    val phone: String,
    @field:Schema(description = "Street address", example = "Av. Paulista")
    val street: String,
    @field:Schema(description = "Street number", example = "1000")
    val number: String,
    @field:Schema(description = "Address complement")
    val complement: String?,
    @field:Schema(description = "Neighborhood", example = "Bela Vista")
    val neighborhood: String,
    @field:Schema(description = "City", example = "São Paulo")
    val city: String,
    @field:Schema(description = "State", example = "SP")
    val state: String,
    @field:Schema(description = "Zip code", example = "01310-100")
    val zipCode: String,
    @field:Schema(description = "User status", example = "ACTIVE")
    val status: UserStatus,
    @field:Schema(description = "Creation timestamp")
    val createdAt: OffsetDateTime,
    @field:Schema(description = "Last update timestamp")
    val updatedAt: OffsetDateTime,
)

@Schema(description = "Account summary for user response")
data class AccountSummaryResponse(
    @field:Schema(description = "Account UUID", example = "550e8400-e29b-41d4-a716-446655440000")
    val id: UUID,
    @field:Schema(description = "Account number", example = "123456789012")
    val accountNumber: String,
    @field:Schema(description = "Branch code", example = "0001")
    val branchCode: String,
    @field:Schema(description = "Account type", example = "CHECKING")
    val type: AccountType,
    @field:Schema(description = "Currency code", example = "BRL")
    val currency: String,
    @field:Schema(description = "Creation timestamp")
    val createdAt: OffsetDateTime,
)

@Schema(description = "Response after user registration")
data class RegisterUserResponse(
    val user: UserResponse,
    val account: AccountSummaryResponse,
)

fun User.toResponse() =
    UserResponse(
        id = id,
        firstName = firstName,
        lastName = lastName,
        email = email,
        cpf = cpf,
        birthDate = birthDate,
        phone = phone,
        street = street,
        number = number,
        complement = complement,
        neighborhood = neighborhood,
        city = city,
        state = state,
        zipCode = zipCode,
        status = status,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

fun Account.toSummary() =
    AccountSummaryResponse(
        id = id,
        accountNumber = accountNumber,
        branchCode = branchCode,
        type = type,
        currency = currency,
        createdAt = createdAt,
    )
