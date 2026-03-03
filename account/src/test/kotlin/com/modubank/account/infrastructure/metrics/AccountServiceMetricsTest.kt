package com.modubank.account.infrastructure.metrics

import io.micrometer.core.instrument.simple.SimpleMeterRegistry
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.concurrent.TimeUnit

class AccountServiceMetricsTest {
    private lateinit var meterRegistry: SimpleMeterRegistry
    private lateinit var metrics: AccountServiceMetrics

    @BeforeEach
    fun setUp() {
        meterRegistry = SimpleMeterRegistry()
        metrics = AccountServiceMetrics(meterRegistry)
    }

    @Test
    fun `should increment user registration counter`() {
        // When
        metrics.incrementUserRegistration()

        // Then
        val counter = meterRegistry.find("account.user.registration.total").counter()
        assert(counter != null)
        assert(counter!!.count() == 1.0)
    }

    @Test
    fun `should increment account creation counter`() {
        // When
        metrics.incrementAccountCreation()

        // Then
        val counter = meterRegistry.find("account.creation.total").counter()
        assert(counter != null)
        assert(counter!!.count() == 1.0)
    }

    @Test
    fun `should increment user not found counter`() {
        // When
        metrics.incrementUserNotFound()

        // Then
        val counter = meterRegistry.find("account.user.notfound.total").counter()
        assert(counter != null)
        assert(counter!!.count() == 1.0)
    }

    @Test
    fun `should increment account not found counter`() {
        // When
        metrics.incrementAccountNotFound()

        // Then
        val counter = meterRegistry.find("account.notfound.total").counter()
        assert(counter != null)
        assert(counter!!.count() == 1.0)
    }

    @Test
    fun `should record user registration time`() {
        // When
        metrics.recordUserRegistrationTime(1_000_000_000L) // 1 second

        // Then
        val timer = meterRegistry.find("account.user.registration.time").timer()
        assert(timer != null)
        assert(timer!!.mean(TimeUnit.NANOSECONDS) == 1_000_000_000.0)
    }

    @Test
    fun `should record account creation time`() {
        // When
        metrics.recordAccountCreationTime(500_000_000L) // 0.5 seconds

        // Then
        val timer = meterRegistry.find("account.creation.time").timer()
        assert(timer != null)
        assert(timer!!.mean(TimeUnit.NANOSECONDS) == 500_000_000.0)
    }

    @Test
    fun `should record user lookup time`() {
        // When
        metrics.recordUserLookupTime(100_000_000L) // 0.1 seconds

        // Then
        val timer = meterRegistry.find("account.user.lookup.time").timer()
        assert(timer != null)
        assert(timer!!.mean(TimeUnit.NANOSECONDS) == 100_000_000.0)
    }
}
