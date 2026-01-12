package com.modubank.account.interfaces.api

import com.modubank.account.application.usecases.GetUser
import com.modubank.account.application.usecases.GetUserAccounts
import com.modubank.account.application.usecases.RegisterUser
import com.modubank.account.application.usecases.RegisterUserCommand
import com.modubank.account.interfaces.api.dto.*
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/v1/users")
@Validated
class UserController(
    private val registerUser: RegisterUser,
    private val getUser: GetUser,
    private val getUserAccounts: GetUserAccounts,
) {
    private val log = LoggerFactory.getLogger(UserController::class.java)

    @PostMapping
    fun register(
        @RequestBody req: RegisterUserRequest,
    ): ResponseEntity<RegisterUserResponse> {
        log.info("Received register user request email={}", req.email)

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
            RegisterUserResponse(
                user = user.toResponse(),
                account = account.toSummary(),
            ),
        )
    }

    @GetMapping("{id}")
    fun getUser(
        @PathVariable id: UUID,
    ): ResponseEntity<UserResponse> {
        val user = getUser.byId(id)
        return ResponseEntity.ok(user.toResponse())
    }

    @GetMapping("{id}/accounts")
    fun getUserAccounts(
        @PathVariable id: UUID,
    ): ResponseEntity<List<AccountSummaryResponse>> {
        val accounts = getUserAccounts.byUserId(id)
        return ResponseEntity.ok(accounts.map { it.toSummary() })
    }
}
