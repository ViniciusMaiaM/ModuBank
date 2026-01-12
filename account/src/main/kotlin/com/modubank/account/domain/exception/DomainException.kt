package com.modubank.account.domain.exception

open class DomainException(
    val code: String,
    message: String? = null,
) : RuntimeException(message ?: code)
