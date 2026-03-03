package com.modubank.account.infrastructure.metrics

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import org.springframework.stereotype.Component

@Component
class AccountServiceMetrics(
    private val meterRegistry: MeterRegistry,
) {
    private val userRegistrationTimer =
        Timer.builder("account.user.registration.time")
            .description("Time taken to register a new user")
            .register(meterRegistry)

    private val accountCreationTimer =
        Timer.builder("account.creation.time")
            .description("Time taken to create an account")
            .register(meterRegistry)

    private val userRegistrationCounter =
        Counter.builder("account.user.registration.total")
            .description("Total number of user registrations")
            .register(meterRegistry)

    private val accountCreationCounter =
        Counter.builder("account.creation.total")
            .description("Total number of accounts created")
            .register(meterRegistry)

    private val userNotFoundCounter =
        Counter.builder("account.user.notfound.total")
            .description("Total number of user not found errors")
            .register(meterRegistry)

    private val accountNotFoundCounter =
        Counter.builder("account.notfound.total")
            .description("Total number of account not found errors")
            .register(meterRegistry)

    fun recordUserRegistrationTime(timeNanos: Long) {
        userRegistrationTimer.record(timeNanos, java.util.concurrent.TimeUnit.NANOSECONDS)
    }

    fun recordAccountCreationTime(timeNanos: Long) {
        accountCreationTimer.record(timeNanos, java.util.concurrent.TimeUnit.NANOSECONDS)
    }

    fun incrementUserRegistration() {
        userRegistrationCounter.increment()
    }

    fun incrementAccountCreation() {
        accountCreationCounter.increment()
    }

    fun incrementUserNotFound() {
        userNotFoundCounter.increment()
    }

    fun incrementAccountNotFound() {
        accountNotFoundCounter.increment()
    }
}
