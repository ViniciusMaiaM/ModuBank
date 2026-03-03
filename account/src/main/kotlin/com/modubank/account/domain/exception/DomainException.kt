package com.modubank.account.domain.exception

open class DomainException(
    val code: String,
    message: String? = null,
    val details: Map<String, Any>? = null,
) : RuntimeException(message ?: code)
