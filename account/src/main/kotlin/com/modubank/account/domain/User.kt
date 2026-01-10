package com.modubank.account.domain

import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.UUID

enum class UserStatus {
    ACTIVE,
    BLOCKED,
}

data class User(
    val id: UUID = UUID.randomUUID(),
    val firstName: String,
    val lastName: String,
    val email: String,
    val passwordHash: String,
    val cpf: String,
    val birthDate: LocalDate,
    val phone: String,
    val street: String,
    val number: String,
    val complement: String? = null,
    val neighborhood: String,
    val city: String,
    val state: String,
    val zipCode: String,
    val status: UserStatus = UserStatus.ACTIVE,
    val createdAt: OffsetDateTime = OffsetDateTime.now(),
    val updatedAt: OffsetDateTime = OffsetDateTime.now(),
)
