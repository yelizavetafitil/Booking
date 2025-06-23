package com.example.booking.data

import com.example.booking.models.RegistrationData
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

    suspend fun registerUser(data: RegistrationData): String {
        return withContext(Dispatchers.IO) {
            try {
                val response = client.post("http://10.0.2.2:8080/register") {
                    contentType(ContentType.Application.Json)
                    setBody(data)
                }
                println("Response status: ${response.status}")
                println("Response body: ${response.body<String>()}")
                response.body()
            } catch (e: Exception) {
                println("Ошибка регистрации: ${e.message}")
                if (e is ResponseException) {
                    println("Статус ответа: ${e.response.status}")
                    println("Тело ответа: ${e.response.body<String>()}")
                }
                throw e
            }
        }
    }
}