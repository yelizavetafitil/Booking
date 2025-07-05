package com.example.booking.models

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class Service(
    val id: Int,
    val serviceName: String,
    val price: Double,
    val currency: String,
    val length: Int,
    val breakDuration: Int
)

@OptIn(InternalSerializationApi::class)
@Serializable
data class ServiceData(
    val enterpriseId: Int,
    val serviceName: String,
    val price: Double,
    val currency: String,
    val length: Int,
    val breakDuration: Int
)

@OptIn(InternalSerializationApi::class)
@Serializable
data class ServiceEdit(
    val serviceName: String,
    val price: Double,
    val currency: String,
    val length: Int,
    val breakDuration: Int
)