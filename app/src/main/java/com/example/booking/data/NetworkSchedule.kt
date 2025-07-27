package com.example.booking.data

import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import com.example.booking.models.EmployeeScheduleResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class NetworkSchedule {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        install(Logging) {
            level = LogLevel.ALL
        }
        expectSuccess = false
    }

    suspend fun getDailySchedules(date: String): List<EmployeeScheduleResponse> {
        return withContext(Dispatchers.IO) {
            try {
                client.get("http://10.0.2.2:8080/api/schedules/day") {
                    parameter("date", date)
                }.body()
            } catch (e: Exception) {
                emptyList()
            }
        }
    }
}