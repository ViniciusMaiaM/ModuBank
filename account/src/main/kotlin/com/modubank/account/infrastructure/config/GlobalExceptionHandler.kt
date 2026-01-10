package com.modubank.account.infrastructure.config

import com.modubank.account.application.usecases.RegisterUser
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.ConstraintViolationException
import org.slf4j.LoggerFactory
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.OffsetDateTime

@RestControllerAdvice
class GlobalExceptionHandler {
    private val log = LoggerFactory.getLogger(RegisterUser::class.java)

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        request: HttpServletRequest,
    ): ResponseEntity<ProblemDetail> {
        val errors =
            ex.bindingResult.fieldErrors.associate { it.field to (it.defaultMessage ?: "invalid") }

        log.warn(
            "Request validation failed path={}, errors={}",
            request.requestURI,
            errors,
        )

        val problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST)
        problem.title = "Validation error"
        problem.detail = "Invalid request payload"
        problem.setProperty("errors", errors)
        enrich(problem, request)

        return ResponseEntity.badRequest().body(problem)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolation(
        ex: ConstraintViolationException,
        request: HttpServletRequest,
    ): ResponseEntity<ProblemDetail> {
        log.warn(
            "Constraint violation path={}, message={}",
            request.requestURI,
            ex.message,
        )

        val problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST)
        problem.title = "Constraint violation"
        problem.detail = ex.message ?: "Invalid request parameters"
        enrich(problem, request)

        return ResponseEntity.badRequest().body(problem)
    }

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFound(
        ex: NoSuchElementException,
        request: HttpServletRequest,
    ): ResponseEntity<ProblemDetail> {
        log.info(
            "Resource not found path={}, message={}",
            request.requestURI,
            ex.message,
        )

        val problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND)
        problem.title = "Resource not found"
        problem.detail = ex.message ?: "Resource not found"
        enrich(problem, request)

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem)
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrityViolation(
        ex: DataIntegrityViolationException,
        request: HttpServletRequest,
    ): ResponseEntity<ProblemDetail> {
        log.warn(
            "Data integrity violation path={}, message={}",
            request.requestURI,
            ex.rootCause?.message,
        )

        val problem = ProblemDetail.forStatus(HttpStatus.CONFLICT)
        problem.title = "Conflict"
        problem.detail = "Resource already exists or violates constraints"
        enrich(problem, request)

        return ResponseEntity.status(HttpStatus.CONFLICT).body(problem)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(
        ex: IllegalArgumentException,
        request: HttpServletRequest,
    ): ResponseEntity<ProblemDetail> {
        log.warn(
            "Business validation error path={}, message={}",
            request.requestURI,
            ex.message,
        )

        val problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST)
        problem.title = "Business validation error"
        problem.detail = ex.message
        enrich(problem, request)

        return ResponseEntity.badRequest().body(problem)
    }

    @ExceptionHandler(Exception::class)
    fun handleUnexpected(
        ex: Exception,
        request: HttpServletRequest,
    ): ResponseEntity<ProblemDetail> {
        log.error(
            "Unexpected error path={}",
            request.requestURI,
            ex,
        )

        val problem = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR)
        problem.title = "Internal server error"
        problem.detail = "unexpected_error"
        enrich(problem, request)

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem)
    }

    private fun enrich(
        problem: ProblemDetail,
        request: HttpServletRequest,
    ) {
        problem.setProperty("timestamp", OffsetDateTime.now())
        problem.setProperty("correlationId", request.getHeader("X-Correlation-Id"))
        problem.setProperty("path", request.requestURI)
    }
}
