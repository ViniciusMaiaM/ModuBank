package com.modubank.account.integration.config

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource

@TestConfiguration
class IntegrationTestConfig {
    companion object {
        @JvmStatic
        @DynamicPropertySource
        fun registerProperties(registry: DynamicPropertyRegistry) {
            val dbUrl = System.getProperty("DB_URL") ?: "jdbc:postgresql://localhost:5432/modubank_test"
            val dbUser = System.getProperty("DB_USER") ?: "modubank"
            val dbPassword = System.getProperty("DB_PASSWORD") ?: "modubank"

            registry.add("spring.datasource.url") { dbUrl }
            registry.add("spring.datasource.username") { dbUser }
            registry.add("spring.datasource.password") { dbPassword }

            registry.add("spring.jpa.hibernate.ddl-auto") { "validate" }
            registry.add("spring.flyway.enabled") { true }
        }
    }
}
