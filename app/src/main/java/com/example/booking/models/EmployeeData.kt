package com.example.booking.models

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class Employee(
    val id: Int,
    val employee_fio: String,
    val employee_phone: String,
    val position: String,
    val access: String
)

@OptIn(InternalSerializationApi::class)
@Serializable
data class EmployeeData(
    val enterpriseId: Int,
    val employee_fio: String,
    val employee_phone: String,
    val position: String,
    val access: String
)

@OptIn(InternalSerializationApi::class)
@Serializable
data class EmployeeEdit(
    val employee_fio: String,
    val employee_phone: String,
    val position: String,
    val access: String
)