package com.example.booking.models

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class EnterpriseData(
    val enterpriseName: String,
    val userId: Int,
    val enterpriseId: Int,
    val access: String,
)