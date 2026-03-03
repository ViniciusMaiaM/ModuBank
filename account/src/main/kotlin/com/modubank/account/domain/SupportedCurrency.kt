package com.modubank.account.domain

enum class SupportedCurrency(
    val code: String,
    val decimalPlaces: Int,
) {
    BRL("BRL", 2),
    USD("USD", 2),
    EUR("EUR", 2),
    GBP("GBP", 2),
    JPY("JPY", 0),
    ;

    companion object {
        fun fromCode(code: String): SupportedCurrency? = entries.find { it.code.equals(code, ignoreCase = true) }

        fun isValid(code: String): Boolean {
            return fromCode(code) != null
        }
    }
}
