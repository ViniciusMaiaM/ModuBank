package com.modubank.account.domain.exception

import java.util.UUID

class AccountNotFoundException(accountId: UUID) : DomainException("account_not_found", "Account not found with id: $accountId")
