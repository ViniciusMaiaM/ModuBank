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
            registry.add("spring.datasource.url") { "jdbc:postgresql://localhost:5432/modubank_test" }
            registry.add("spring.datasource.username") { "modubank" }
            registry.add("spring.datasource.password") { "modubank" }

            registry.add("spring.jpa.hibernate.ddl-auto") { "validate" }
            registry.add("spring.flyway.enabled") { true }
        }
    }
}
