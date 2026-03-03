package com.modubank.account.infrastructure.health

import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.HealthIndicator
import org.springframework.stereotype.Component
import javax.sql.DataSource

@Component
class DatabaseHealthIndicator(
    private val dataSource: DataSource,
) : HealthIndicator {
    override fun health(): Health {
        return try {
            dataSource.connection.use { connection ->
                if (connection.isValid(5)) {
                    Health.up()
                        .withDetail("database", "PostgreSQL")
                        .withDetail("validationQuery", "SELECT 1")
                        .build()
                } else {
                    Health.down()
                        .withDetail("error", "Connection invalid")
                        .build()
                }
            }
        } catch (e: Exception) {
            Health.down()
                .withDetail("database", "PostgreSQL")
                .withDetail("error", e.message)
                .build()
        }
    }
}
