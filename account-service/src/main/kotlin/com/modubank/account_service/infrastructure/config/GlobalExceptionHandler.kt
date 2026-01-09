package com.modubank.account_service.infrastructure.config

import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(ex: MethodArgumentNotValidException): ResponseEntity<ProblemDetail> {
        val errors =
            ex.bindingResult.fieldErrors.associate { it.field to (it.defaultMessage ?: "invalid") }
        val pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failed")
        pd.title = "Bad Request"
        pd.setProperty("errors", errors)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(pd)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraint(ex: ConstraintViolationException): ResponseEntity<ProblemDetail> {
        val pd =
            ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                ex.message ?: "Constraint violation",
            )
        pd.title = "Bad Request"
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(pd)
    }

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFound(ex: NoSuchElementException): ResponseEntity<ProblemDetail> {
        val pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.message ?: "Not found")
        pd.title = "Not Found"
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(pd)
    }

    @ExceptionHandler(Exception::class)
    fun handleGeneric(ex: Exception): ResponseEntity<ProblemDetail> {
        val pd =
            ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error")
        pd.title = "Internal Server Error"
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(pd)
    }

    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException::class)
    fun handleConflict(
        ex: org.springframework.dao.DataIntegrityViolationException,
    ): ResponseEntity<ProblemDetail> {
        val pd =
            ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, "Unique constraint violation")
        pd.title = "Conflict"
        return ResponseEntity.status(HttpStatus.CONFLICT).body(pd)
    }
}
