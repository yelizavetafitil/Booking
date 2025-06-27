package com.example.booking.data

import com.example.booking.models.*
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


class NetworkEnterprise {
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

    suspend fun registerEnterprise(data: EnterpriseRegistrationData): Int {
        return withContext(Dispatchers.IO) {
            try {
                val response = client.post("http://10.0.2.2:8080/enterpriseRegistration") {
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
                val responseBody: EnterpriseRegistrationResponse = response.body()
                println("Registered enterprise ID: ${responseBody.enterpriseId}")
                responseBody.enterpriseId
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

    suspend fun backEnterprise(userId: Int) {
        withContext(Dispatchers.IO) {
            try {
                val response = client.delete("http://10.0.2.2:8080/enterpriseBack/$userId") {
                }
                println("Response status: ${response.status}")

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