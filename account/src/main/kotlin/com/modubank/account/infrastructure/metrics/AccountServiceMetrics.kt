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

    private val userLookupTimer =
        Timer.builder("account.user.lookup.time")
            .description("Time taken to lookup a user by ID")
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

    private val userUpdateCounter =
        Counter.builder("account.user.update.total")
            .description("Total number of user updates")
            .register(meterRegistry)

    private val userUpdateTimer =
        Timer.builder("account.user.update.time")
            .description("Time taken to update a user")
            .register(meterRegistry)

    private val accountUpdateCounter =
        Counter.builder("account.update.total")
            .description("Total number of account updates")
            .register(meterRegistry)

    private val accountUpdateTimer =
        Timer.builder("account.update.time")
            .description("Time taken to update an account")
            .register(meterRegistry)

    private val userDeletionCounter =
        Counter.builder("account.user.deletion.total")
            .description("Total number of user deletions")
            .register(meterRegistry)

    private val userDeletionTimer =
        Timer.builder("account.user.deletion.time")
            .description("Time taken to delete a user")
            .register(meterRegistry)

    private val accountDeletionCounter =
        Counter.builder("account.deletion.total")
            .description("Total number of account deletions")
            .register(meterRegistry)

    private val accountDeletionTimer =
        Timer.builder("account.deletion.time")
            .description("Time taken to delete an account")
            .register(meterRegistry)

    fun recordUserRegistrationTime(timeNanos: Long) {
        userRegistrationTimer.record(timeNanos, java.util.concurrent.TimeUnit.NANOSECONDS)
    }

    fun incrementAccountCreation() {
        accountCreationCounter.increment()
    }

    fun recordAccountCreationTime(timeNanos: Long) {
        accountCreationTimer.record(timeNanos, java.util.concurrent.TimeUnit.NANOSECONDS)
    }

    fun recordUserLookupTime(timeNanos: Long) {
        userLookupTimer.record(timeNanos, java.util.concurrent.TimeUnit.NANOSECONDS)
    }

    fun incrementUserRegistration() {
        userRegistrationCounter.increment()
    }

    fun incrementUserNotFound() {
        userNotFoundCounter.increment()
    }

    fun incrementAccountNotFound() {
        accountNotFoundCounter.increment()
    }

    fun incrementUserUpdate() {
        userUpdateCounter.increment()
    }

    fun recordUserUpdateTime(timeNanos: Long) {
        userUpdateTimer.record(timeNanos, java.util.concurrent.TimeUnit.NANOSECONDS)
    }

    fun incrementAccountUpdate() {
        accountUpdateCounter.increment()
    }

    fun recordAccountUpdateTime(timeNanos: Long) {
        accountUpdateTimer.record(timeNanos, java.util.concurrent.TimeUnit.NANOSECONDS)
    }

    fun incrementUserDeletion() {
        userDeletionCounter.increment()
    }

    fun recordUserDeletionTime(timeNanos: Long) {
        userDeletionTimer.record(timeNanos, java.util.concurrent.TimeUnit.NANOSECONDS)
    }

    fun incrementAccountDeletion() {
        accountDeletionCounter.increment()
    }

    fun recordAccountDeletionTime(timeNanos: Long) {
        accountDeletionTimer.record(timeNanos, java.util.concurrent.TimeUnit.NANOSECONDS)
    }
}
