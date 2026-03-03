package com.modubank.account

import com.modubank.account.integration.config.IntegrationTestConfig
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
@ContextConfiguration(classes = [IntegrationTestConfig::class])
class AccountServiceApplicationTests {
    @Test fun contextLoads() {}
}
