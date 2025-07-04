package com.example.booking.models
import android.annotation.SuppressLint
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
data class UserData(
    val fullName: String,
    val phoneNumber: String,
    val password: String
)

@OptIn(InternalSerializationApi::class)
@Serializable
data class RegistrationResponse(
    val userId: Int
)

@OptIn(InternalSerializationApi::class)
@Serializable
data class EnterpriseRegistrationData(
    val enterpriseName: String,
    val city: String,
    val address: String,
    val enterprisePhoneNumber: String,
    val userId: Int
)

@OptIn(InternalSerializationApi::class)
@Serializable
data class EnterpriseRegistrationResponse(
    val enterpriseId: Int
)