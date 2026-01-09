package com.modubank.account_service.interfaces.api

import com.modubank.account_service.application.repositories.AccountRepository
import com.modubank.account_service.application.repositories.UserRepository
import com.modubank.account_service.application.usecases.RegisterUser
import com.modubank.account_service.application.usecases.RegisterUserCommand
import com.modubank.account_service.interfaces.api.dto.*
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/v1/users")
@Validated
class UserController(
    private val registerUser: RegisterUser,
    private val userRepo: UserRepository,
    private val accountRepo: AccountRepository,
) {

    @PostMapping
    fun register(
        @Valid @RequestBody req: RegisterUserRequest,
    ): ResponseEntity<RegisterUserResponse> {
        val (user, account) =
            registerUser.execute(
                RegisterUserCommand(
                    firstName = req.firstName,
                    lastName = req.lastName,
                    email = req.email,
                    password = req.password,
                    cpf = req.cpf,
                    birthDate = req.birthDate,
                    phone = req.phone,
                    street = req.street,
                    number = req.number,
                    complement = req.complement,
                    neighborhood = req.neighborhood,
                    city = req.city,
                    state = req.state,
                    zipCode = req.zipCode,
                    currency = req.currency,
                    accountType = req.accountType,
                ),
            )
        return ResponseEntity.ok(
            RegisterUserResponse(user = user.toResponse(), account = account.toSummary()),
        )
    }

    @GetMapping("{id}")
    fun getUser(@PathVariable id: UUID): ResponseEntity<UserResponse> =
        userRepo
            .findById(id)
            .map { ResponseEntity.ok(it.toResponse()) }
            .orElse(ResponseEntity.notFound().build())

    @GetMapping("{id}/accounts")
    fun getUserAccounts(@PathVariable id: UUID): ResponseEntity<List<AccountSummaryResponse>> =
        ResponseEntity.ok(accountRepo.findByUserId(id).map { it.toSummary() })
}
