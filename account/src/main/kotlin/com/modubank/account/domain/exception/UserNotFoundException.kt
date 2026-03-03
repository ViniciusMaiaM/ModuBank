package com.modubank.account.domain.exception

import java.util.UUID

class UserNotFoundException(userId: UUID) : DomainException("user_not_found", "User not found with id: $userId")
