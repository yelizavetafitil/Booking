package com.example.booking.data

import com.example.booking.models.EmployeeData
import com.example.booking.models.RecordData
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.time.LocalDate

class NetworkRecord {
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
            logger = object : Logger {
                override fun log(message: String) {
                    println("HTTP Client: $message")
                }
            }
        }
        expectSuccess = false
    }

    suspend fun getTimeSlots(employeeId: Int, serviceId: Int, date: LocalDate): Map<String, List<String>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = client.get("http://10.0.2.2:8080/timeslots") {
                    parameter("employeeId", employeeId)
                    parameter("serviceId", serviceId)
                    parameter("date", date.toString())
                }

                if (response.status == HttpStatusCode.OK) {
                    response.body()
                } else {
                    throw Exception("Failed to fetch time slots: ${response.status}")
                }
            } catch (e: ResponseException) {
                throw Exception("Server error: ${e.response.status}")
            } catch (e: Exception) {
                throw Exception("Network error: ${e.localizedMessage}")
            }
        }
    }


    suspend fun getServiceName(serviceId: Int): String {
        return withContext(Dispatchers.IO) {
            try {
                val response = client.get("http://10.0.2.2:8080/services/$serviceId/name")

                if (response.status == HttpStatusCode.OK) {
                    response.body()
                } else {
                    throw Exception("Failed to fetch service name: ${response.status}")
                }
            } catch (e: ResponseException) {
                throw Exception("Server error: ${e.response.status}")
            } catch (e: Exception) {
                throw Exception("Network error: ${e.localizedMessage}")
            }
        }
    }


    suspend fun getEmployeeName(employeeId: Int): String {
        return withContext(Dispatchers.IO) {
            try {
                val response = client.get("http://10.0.2.2:8080/employees/$employeeId/name")

                if (response.status == HttpStatusCode.OK) {
                    response.body()
                } else {
                    throw Exception("Failed to fetch employee name: ${response.status}")
                }
            } catch (e: ResponseException) {
                throw Exception("Server error: ${e.response.status}")
            } catch (e: Exception) {
                throw Exception("Network error: ${e.localizedMessage}")
            }
        }
    }

    suspend fun addRecord(data: RecordData) {
        withContext(Dispatchers.IO) {
            try {
                val response = client.post("http://10.0.2.2:8080/addRecord") {
                    contentType(ContentType.Application.Json)
                    setBody(data)
                    accept(ContentType.Application.Json)
                }
                println("Response status: ${response.status}")
                if (response.status == HttpStatusCode.Conflict) {
                    val errorMessage = response.body<String>()
                    throw Exception(errorMessage)
                }
                if (response.status == HttpStatusCode.BadRequest) {
                    val errorMessage = response.body<String>()
                    throw Exception(errorMessage)
                }
            } catch (e: Exception) {
                println("Ошибка: ${e.message}")
                if (e is ResponseException) {
                    println("Статус ответа: ${e.response.status}")
                    println("Тело ответа: ${e.response.body<String>()}")
                }
                throw e
            }
        }
    }
}