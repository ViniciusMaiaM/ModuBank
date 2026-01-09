package com.modubank.account_service.domain

import java.time.OffsetDateTime
import java.util.UUID

enum class AccountStatus {
    ACTIVE,
    BLOCKED,
    CLOSED,
}

enum class AccountType {
    CHECKING,
    SAVINGS,
}

data class Account(
    val id: UUID = UUID.randomUUID(),
    val userId: UUID,
    val currency: String,
    val status: AccountStatus = AccountStatus.ACTIVE,
    val accountNumber: String,
    val branchCode: String = "0001",
    val type: AccountType = AccountType.CHECKING,
    val createdAt: OffsetDateTime = OffsetDateTime.now(),
)
