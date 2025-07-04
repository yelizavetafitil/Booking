package com.example.booking.data

import com.example.booking.models.RegistrationData
import com.example.booking.models.RegistrationResponse
import com.example.booking.models.UserData
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json


class NetworkClient {
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

    suspend fun registerUser(data: RegistrationData): Int {
        return withContext(Dispatchers.IO) {
            try {
                val response = client.post("http://10.0.2.2:8080/register") {
                    contentType(ContentType.Application.Json)
                    setBody(data)
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
                val responseBody: RegistrationResponse = response.body()
                println("Registered user ID: ${responseBody.userId}")
                responseBody.userId
            } catch (e: Exception) {
                println("Ошибка регистрации: ${e.message}")
                if (e is ResponseException) {
                    println("Статус ответа: ${e.response.status}")
                    println("Тело ответа: ${e.response.body<String>()}")
                }
                throw e
            } as Int
        }
    }

    suspend fun loadUserProfile(userId: Int): UserData {
        return withContext(Dispatchers.IO) {
            try {
                val response = client.get("http://10.0.2.2:8080/users/$userId") {
                    contentType(ContentType.Application.Json)
                }

                when (response.status) {
                    HttpStatusCode.OK -> {
                        response.body<UserData>().also {
                            println("Successfully loaded user profile: $it")
                        } ?: throw Exception("Empty response body")
                    }
                    HttpStatusCode.NotFound -> throw Exception("User not found")
                    else -> throw Exception("Server error: ${response.status}")
                }
            } catch (e: Exception) {
                println("Error loading user profile: ${e.message}")
                throw when (e) {
                    is ResponseException -> Exception("Network error: ${e.response.bodyAsText()}")
                    else -> Exception("Failed to load profile: ${e.message}")
                }
            }
        }
    }

    suspend fun updateUserProfile(userId: Int, data: UserData): Boolean {
        val response = client.put("http://10.0.2.2:8080/userUpdate/$userId") {
            contentType(ContentType.Application.Json)
            setBody(data)
        }

        return response.status.isSuccess()
    }
}