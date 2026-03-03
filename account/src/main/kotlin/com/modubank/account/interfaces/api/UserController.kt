package com.modubank.account.interfaces.api

import com.modubank.account.application.usecases.DeleteUser
import com.modubank.account.application.usecases.GetUser
import com.modubank.account.application.usecases.GetUserAccounts
import com.modubank.account.application.usecases.RegisterUser
import com.modubank.account.application.usecases.RegisterUserCommand
import com.modubank.account.application.usecases.UpdateUser
import com.modubank.account.application.usecases.UpdateUserCommand
import com.modubank.account.infrastructure.metrics.AccountServiceMetrics
import com.modubank.account.interfaces.api.dto.*
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
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
    private val updateUser: UpdateUser,
    private val deleteUser: DeleteUser,
    private val metrics: AccountServiceMetrics,
) {
    private val log = LoggerFactory.getLogger(UserController::class.java)

    @PostMapping
    fun register(
        @RequestBody req: RegisterUserRequest,
    ): ResponseEntity<RegisterUserResponse> {
        log.info("Registering new user email={}", req.email)
        val startTime = System.nanoTime()

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

        metrics.recordUserRegistrationTime(System.nanoTime() - startTime)
        metrics.incrementUserRegistration()

        log.info("User registered successfully userId={}, accountId={}", user.id, account.id)
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(
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
        val startTime = System.nanoTime()

        val user = getUser.byId(id)
        metrics.recordUserLookupTime(System.nanoTime() - startTime)
        return ResponseEntity.ok(user.toResponse())
    }

    @GetMapping("{id}/accounts")
    fun getUserAccounts(
        @PathVariable id: UUID,
    ): ResponseEntity<List<AccountSummaryResponse>> {
        val accounts = getUserAccounts.byUserId(id)
        return ResponseEntity.ok(accounts.map { it.toSummary() })
    }

    @PutMapping("{id}")
    fun updateUser(
        @PathVariable id: UUID,
        @RequestBody req: UpdateUserRequest,
    ): ResponseEntity<UserResponse> {
        log.info("Updating user userId={}", id)
        val startTime = System.nanoTime()

        val user =
            updateUser.execute(
                UpdateUserCommand(
                    id = id,
                    firstName = req.firstName,
                    lastName = req.lastName,
                    email = req.email,
                    phone = req.phone,
                    street = req.street,
                    number = req.number,
                    complement = req.complement,
                    neighborhood = req.neighborhood,
                    city = req.city,
                    state = req.state,
                    zipCode = req.zipCode,
                ),
            )
        metrics.recordUserUpdateTime(System.nanoTime() - startTime)
        metrics.incrementUserUpdate()
        log.info("User updated successfully userId={}", user.id)
        return ResponseEntity.ok(user.toResponse())
    }

    @DeleteMapping("{id}")
    fun deleteUser(
        @PathVariable id: UUID,
    ): ResponseEntity<Void> {
        log.info("Deleting user userId={}", id)
        val startTime = System.nanoTime()

        deleteUser.execute(id)
        metrics.recordUserDeletionTime(System.nanoTime() - startTime)
        metrics.incrementUserDeletion()
        log.info("User deleted successfully userId={}", id)
        return ResponseEntity.noContent().build()
    }
}
