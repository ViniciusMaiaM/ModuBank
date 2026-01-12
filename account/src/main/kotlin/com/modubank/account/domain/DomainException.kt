package com.modubank.account.domain

open class DomainException(
    val code: String,
    message: String? = null,
) : RuntimeException(message ?: code)
