package com.modubank.account.domain.exception

import com.modubank.account.domain.SupportedCurrency

class UnsupportedCurrencyException(currency: String) : DomainException(
    code = "currency_not_supported",
    message = "currency_not_supported",
    details =
        mapOf(
            "currency" to currency,
            "supported" to SupportedCurrency.entries.map { it.code },
        ),
)
