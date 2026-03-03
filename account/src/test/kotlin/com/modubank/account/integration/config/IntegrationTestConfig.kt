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
            // Para testes de integração, usa PostgreSQL se disponível, senão H2
            val dbUrl = System.getProperty("DB_URL") ?: "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE"
            val dbUser = System.getProperty("DB_USER") ?: "sa"
            val dbPassword = System.getProperty("DB_PASSWORD") ?: ""

            registry.add("spring.datasource.url") { dbUrl }
            registry.add("spring.datasource.username") { dbUser }
            registry.add("spring.datasource.password") { dbPassword }

            // Ajusta configurações baseadas no tipo de banco
            if (dbUrl.contains("postgresql")) {
                registry.add("spring.jpa.hibernate.ddl-auto") { "validate" }
                registry.add("spring.flyway.enabled") { true }
            } else {
                registry.add("spring.jpa.hibernate.ddl-auto") { "create-drop" }
                registry.add("spring.flyway.enabled") { false }
            }
        }
    }
}
