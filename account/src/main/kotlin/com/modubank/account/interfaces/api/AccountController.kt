package com.modubank.account.interfaces.api

import com.modubank.account.application.usecases.CreateAccount
import com.modubank.account.application.usecases.CreateAccountCommand
import com.modubank.account.application.usecases.GetAccount
import com.modubank.account.application.usecases.RegisterUser
import com.modubank.account.interfaces.api.dto.*
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/v1/accounts")
@Validated
class AccountController(
    private val createAccount: CreateAccount,
    private val getAccount: GetAccount,
) {
    private val log = LoggerFactory.getLogger(RegisterUser::class.java)

    @PostMapping
    fun create(
        @RequestBody request: CreateAccountRequest,
    ): ResponseEntity<ApiResponse<AccountResponse>> {
        log.info(
            "Received create account request userId={}, currency={}",
            request.userId,
            request.currency,
        )

        val account =
            createAccount.execute(
                CreateAccountCommand(
                    userId = request.userId,
                    currency = request.currency,
                ),
            )

        log.info("Returning create account response accountId={}", account.id)

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse(account.toResponse()))
    }

    @GetMapping("{id}")
    fun get(
        @PathVariable id: UUID,
    ): ResponseEntity<AccountResponse> {
        log.info("Received get account request accountId={}", id)

        return getAccount
            .byId(id)
            .map {
                log.info("Account found accountId={}", id)
                ResponseEntity.ok(it.toResponse())
            }
            .orElseGet {
                log.warn("Account not found accountId={}", id)
                ResponseEntity.notFound().build()
            }
    }

    @GetMapping("{id}/balance")
    fun balance(
        @PathVariable id: UUID,
    ): ResponseEntity<Void> {
        log.info("Balance endpoint called accountId={} (not implemented)", id)
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build()
    }
}
