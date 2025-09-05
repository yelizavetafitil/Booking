package com.example.booking.models


import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class RecordData(
    val date: String,
    val time: String,
    val recordFio: String,
    val recordPhone: String,
    val comment: String?,
    val idServices: Int?,
    val idEmployee: Int?,
    val idCompany: Int?
)
