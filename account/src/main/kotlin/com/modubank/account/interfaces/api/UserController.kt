package com.modubank.account.interfaces.api

import com.modubank.account.application.repositories.AccountRepository
import com.modubank.account.application.repositories.UserRepository
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
    private val userRepo: UserRepository,
    private val accountRepo: AccountRepository,
) {
    private val log = LoggerFactory.getLogger(RegisterUser::class.java)

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

        log.info(
            "User registration completed userId={}, accountId={}",
            user.id,
            account.id,
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
        log.info("Fetching user id={}", id)

        return userRepo
            .findById(id)
            .map {
                log.info("User found id={}", id)
                ResponseEntity.ok(it.toResponse())
            }
            .orElseGet {
                log.warn("User not found id={}", id)
                ResponseEntity.notFound().build()
            }
    }

    @GetMapping("{id}/accounts")
    fun getUserAccounts(
        @PathVariable id: UUID,
    ): ResponseEntity<List<AccountSummaryResponse>> {
        log.info("Fetching accounts for user id={}", id)
        return ResponseEntity.ok(
            accountRepo.findByUserId(id).map { it.toSummary() },
        )
    }
}
