package com.modubank.account.infrastructure.config

import com.modubank.account.domain.exception.DomainException
import com.modubank.account.domain.exception.UnsupportedCurrencyException
import jakarta.servlet.http.HttpServletRequest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus

@ExtendWith(MockitoExtension::class)
class GlobalExceptionHandlerTest {
    private lateinit var handler: GlobalExceptionHandler

    @Mock
    private lateinit var request: HttpServletRequest

    @BeforeEach
    fun setup() {
        handler = GlobalExceptionHandler()
    }

    @Test
    fun `should handle domain exception`() {
        val exception = DomainException("test_code", "Test message")

        val response = handler.handleDomainException(exception, request)

        org.junit.jupiter.api.Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        val body = response.body
        org.junit.jupiter.api.Assertions.assertEquals("test_code", body?.detail)
    }

    @Test
    fun `should handle unsupported currency exception`() {
        val exception = UnsupportedCurrencyException("INVALID")

        val response = handler.handleDomainException(exception, request)

        org.junit.jupiter.api.Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        val body = response.body
        org.junit.jupiter.api.Assertions.assertEquals("currency_not_supported", body?.detail)
    }

    @Test
    fun `should handle unexpected exception`() {
        val exception = RuntimeException("Unexpected error")

        val response = handler.handleUnexpected(exception, request)

        org.junit.jupiter.api.Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.statusCode)
        val body = response.body
        org.junit.jupiter.api.Assertions.assertEquals("unexpected_error", body?.detail)
    }

    @Test
    fun `should handle data integrity violation`() {
        val exception = DataIntegrityViolationException("Duplicate entry")

        val response = handler.handleDataIntegrityViolation(exception, request)

        org.junit.jupiter.api.Assertions.assertEquals(HttpStatus.CONFLICT, response.statusCode)
    }
}
