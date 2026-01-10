package com.modubank.account.infrastructure.persistence.jpa

import jakarta.persistence.*
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.*

@Entity
@Table(
    name = "users",
    uniqueConstraints =
        [UniqueConstraint(columnNames = ["email"]), UniqueConstraint(columnNames = ["cpf"])],
)
class UserEntity(
    @Id var id: UUID? = null,
    @Column(nullable = false) var firstName: String? = null,
    @Column(nullable = false) var lastName: String? = null,
    @Column(nullable = false) var email: String? = null,
    @Column(nullable = false) var passwordHash: String? = null,
    @Column(nullable = false) var cpf: String? = null,
    @Column(nullable = false) var birthDate: LocalDate? = null,
    @Column(nullable = false) var phone: String? = null,
    @Column(nullable = false) var street: String? = null,
    @Column(nullable = false) var number: String? = null,
    var complement: String? = null,
    @Column(nullable = false) var neighborhood: String? = null,
    @Column(nullable = false) var city: String? = null,
    @Column(name = "state", nullable = false, length = 2) var state: String? = null,
    @Column(nullable = false) var zipCode: String? = null,
    @Column(nullable = false) @Enumerated(EnumType.STRING) var status: UserStatus? = null,
    @Column(nullable = false) var createdAt: OffsetDateTime? = null,
    @Column(nullable = false) var updatedAt: OffsetDateTime? = null,
) {
    enum class UserStatus {
        ACTIVE,
        BLOCKED,
    }
}
