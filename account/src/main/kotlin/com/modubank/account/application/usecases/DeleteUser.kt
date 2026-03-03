package com.modubank.account.application.usecases

import com.modubank.account.application.repositories.AccountRepository
import com.modubank.account.application.repositories.UserRepository
import com.modubank.account.domain.exception.UserNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DeleteUser(
    private val userRepository: UserRepository,
    private val accountRepository: AccountRepository,
) {
    private val log = LoggerFactory.getLogger(DeleteUser::class.java)

    @Transactional
    fun execute(userId: java.util.UUID) {
        log.info("Deleting user userId={}", userId)

        val user =
            userRepository.findById(userId)
                .orElseThrow { UserNotFoundException(userId) }

        // Verificar se usuário tem contas associadas
        val userAccounts = accountRepository.findByUserId(userId)
        if (userAccounts.isNotEmpty()) {
            throw IllegalStateException("Cannot delete user with active accounts")
        }

        userRepository.delete(user)
        log.info("User deleted successfully userId={}", userId)
    }
}
