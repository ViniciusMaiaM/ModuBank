package com.modubank.account.application.usecases

import com.modubank.account.application.repositories.UserRepository
import com.modubank.account.domain.User
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*

@Service
class GetUser(
    private val userRepository: UserRepository,
) {
    private val log = LoggerFactory.getLogger(GetUser::class.java)

    fun byId(id: UUID): User {
        log.info("Fetching user by id={}", id)

        return userRepository.findById(id)
            .orElseThrow {
                log.warn("User not found id={}", id)
                NoSuchElementException("user_not_found")
            }
    }
}
