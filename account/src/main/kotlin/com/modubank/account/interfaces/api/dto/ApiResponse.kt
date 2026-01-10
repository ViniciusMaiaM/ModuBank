package com.modubank.account.interfaces.api.dto

data class ApiResponse<T>(
    val data: T,
    val meta: Meta = Meta(),
)
