package com.modubank.account.interfaces.api.dto

import org.slf4j.MDC
import java.time.Instant

data class Meta(
    val requestId: String = MDC.get("requestId") ?: "N/A",
    val timestamp: Instant = Instant.now(),
)
