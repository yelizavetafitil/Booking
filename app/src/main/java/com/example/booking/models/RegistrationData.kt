package com.example.booking.models
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class RegistrationData(
    val fullName: String,
    val phoneNumber: String,
    val password: String
)

@OptIn(InternalSerializationApi::class)
@Serializable
data class RegistrationResponse(
    val userId: Int
)