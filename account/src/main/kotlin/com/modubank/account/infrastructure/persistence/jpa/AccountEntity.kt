package com.modubank.account.infrastructure.persistence.jpa

import jakarta.persistence.*
import java.time.OffsetDateTime
import java.util.*

@Entity
@Table(name = "accounts", uniqueConstraints = [UniqueConstraint(columnNames = ["account_number"])])
class AccountEntity(
    @Id var id: UUID? = null,
    @Column(name = "user_id", nullable = false) var userId: UUID? = null,
    @Column(nullable = false, length = 3) var currency: String? = null,
    @Column(nullable = false) @Enumerated(EnumType.STRING) var status: AccountStatus? = null,
    @Column(name = "account_number", nullable = false, length = 20)
    var accountNumber: String? = null,
    @Column(name = "branch_code", nullable = false, length = 10) var branchCode: String? = "0001",
    @Column(nullable = false) @Enumerated(EnumType.STRING) var type: AccountType? = null,
    @Column(name = "created_at", nullable = false) var createdAt: OffsetDateTime? = null,
) {
    enum class AccountStatus {
        ACTIVE,
        BLOCKED,
        CLOSED,
    }

    enum class AccountType {
        CHECKING,
        SAVINGS,
    }
}
