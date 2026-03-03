package com.modubank.account.interfaces.api.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UpdateUserRequest(
    @field:NotBlank(message = "First name is required")
    @field:Size(max = 100, message = "First name must be less than 100 characters")
    val firstName: String?,
    @field:NotBlank(message = "Last name is required")
    @field:Size(max = 100, message = "Last name must be less than 100 characters")
    val lastName: String?,
    @field:Email(message = "Invalid email format")
    @field:Size(max = 255, message = "Email must be less than 255 characters")
    val email: String?,
    @field:Size(max = 20, message = "Phone must be less than 20 characters")
    val phone: String?,
    @field:Size(max = 200, message = "Street must be less than 200 characters")
    val street: String?,
    @field:Size(max = 20, message = "Number must be less than 20 characters")
    val number: String?,
    @field:Size(max = 100, message = "Complement must be less than 100 characters")
    val complement: String?,
    @field:Size(max = 100, message = "Neighborhood must be less than 100 characters")
    val neighborhood: String?,
    @field:Size(max = 100, message = "City must be less than 100 characters")
    val city: String?,
    @field:Size(max = 2, message = "State must be 2 characters")
    val state: String?,
    @field:Size(max = 10, message = "Zip code must be less than 10 characters")
    val zipCode: String?,
)
