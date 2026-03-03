package com.modubank.account.infrastructure.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "account")
class AccountConfig {
    var branchCode: String = "0001"
}
