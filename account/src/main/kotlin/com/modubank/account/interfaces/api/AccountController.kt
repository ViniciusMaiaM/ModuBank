package com.modubank.account.interfaces.api

import com.modubank.account.application.usecases.CreateAccount
import com.modubank.account.application.usecases.CreateAccountCommand
import com.modubank.account.application.usecases.DeleteAccount
import com.modubank.account.application.usecases.GetAccount
import com.modubank.account.application.usecases.UpdateAccount
import com.modubank.account.application.usecases.UpdateAccountCommand
import com.modubank.account.infrastructure.metrics.AccountServiceMetrics
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
    private val updateAccount: UpdateAccount,
    private val deleteAccount: DeleteAccount,
    private val metrics: AccountServiceMetrics,
) {
    private val log = LoggerFactory.getLogger(AccountController::class.java)

    @GetMapping("{id}")
    fun get(
        @PathVariable id: UUID,
    ): ResponseEntity<AccountResponse> {
        return getAccount
            .byId(id)
            .map { ResponseEntity.ok(it.toResponse()) }
            .orElseGet {
                metrics.incrementAccountNotFound()
                ResponseEntity.notFound().build()
            }
    }

    @PostMapping
    fun create(
        @RequestBody req: CreateAccountRequest,
    ): ResponseEntity<AccountResponse> {
        val startTime = System.nanoTime()

        val account =
            createAccount.execute(
                CreateAccountCommand(
                    userId = req.userId,
                    currency = req.currency,
                ),
            )

        metrics.recordAccountCreationTime(System.nanoTime() - startTime)
        metrics.incrementAccountCreation()

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(account.toResponse())
    }

    @PutMapping("{id}")
    fun updateAccount(
        @PathVariable id: UUID,
        @RequestBody req: UpdateAccountRequest,
    ): ResponseEntity<AccountResponse> {
        log.info("Updating account accountId={}", id)
        val startTime = System.nanoTime()

        val account =
            updateAccount.execute(
                UpdateAccountCommand(
                    id = id,
                    accountType = req.accountType,
                    currency = req.currency,
                ),
            )
        metrics.recordAccountUpdateTime(System.nanoTime() - startTime)
        metrics.incrementAccountUpdate()
        log.info("Account updated successfully accountId={}", account.id)
        return ResponseEntity.ok(account.toResponse())
    }

    @DeleteMapping("{id}")
    fun deleteAccount(
        @PathVariable id: UUID,
    ): ResponseEntity<Void> {
        log.info("Deleting account accountId={}", id)
        val startTime = System.nanoTime()

        deleteAccount.execute(id)
        metrics.recordAccountDeletionTime(System.nanoTime() - startTime)
        metrics.incrementAccountDeletion()
        log.info("Account deleted successfully accountId={}", id)
        return ResponseEntity.noContent().build()
    }
}
