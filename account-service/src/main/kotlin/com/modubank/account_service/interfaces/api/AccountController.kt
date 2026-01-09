package com.modubank.account_service.interfaces.api

import com.modubank.account_service.application.usecases.CreateAccount
import com.modubank.account_service.application.usecases.CreateAccountCommand
import com.modubank.account_service.application.usecases.GetAccount
import com.modubank.account_service.interfaces.api.dto.AccountResponse
import com.modubank.account_service.interfaces.api.dto.CreateAccountRequest
import com.modubank.account_service.interfaces.api.dto.toResponse
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/v1/accounts")
@Validated
class AccountController(
    private val createAccount: CreateAccount,
    private val getAccount: GetAccount,
) {
    @PostMapping
    fun create(@Valid @RequestBody req: CreateAccountRequest): ResponseEntity<AccountResponse> =
        ResponseEntity.ok(
            createAccount.execute(CreateAccountCommand(req.userId, req.currency)).toResponse(),
        )

    @GetMapping("{id}")
    fun get(@PathVariable id: UUID): ResponseEntity<AccountResponse> =
        getAccount
            .byId(id)
            .map { ResponseEntity.ok(it.toResponse()) }
            .orElse(ResponseEntity.notFound().build())

    @GetMapping("{id}/balance")
    fun balance(@PathVariable id: UUID) = ResponseEntity.status(501).build<Void>()!!
}
