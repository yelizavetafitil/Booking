package com.example.booking.data

import com.example.booking.models.*
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


class NetworkService {
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

    suspend fun addService(data: ServiceData): ServiceAddResponse {
        return try {
            val response = client.post("http://10.0.2.2:8080/addService") {
                contentType(ContentType.Application.Json)
                setBody(data)
            }
            if (response.status == HttpStatusCode.Created) {
                response.body()
            } else {
                throw Exception("Unexpected status: ${response.status}")
            }
        } catch (e: Exception) {
            ServiceAddResponse(
                serviceId = 0,
                success = false,
                message = e.localizedMessage ?: "Unknown error"
            )
        }
    }

    suspend fun getEnterpriseServices(enterpriseId: Int): List<Service> {
        return withContext(Dispatchers.IO) {
            try {
                val response = client.get("http://10.0.2.2:8080/enterpriseServices/$enterpriseId") {
                    accept(ContentType.Application.Json)
                }

                when (response.status) {
                    HttpStatusCode.OK -> response.body()
                    else -> throw Exception("Ошибка сервера: ${response.status}")
                }
            } catch (e: Exception) {
                println("Ошибка получения: ${e.message}")
                throw e
            }
        }
    }

    suspend fun loadService(serviceId: Int): ServiceEdit {
        return withContext(Dispatchers.IO) {
            try {
                val response = client.get("http://10.0.2.2:8080/services/$serviceId") {
                    accept(ContentType.Application.Json)
                }

                when (response.status) {
                    HttpStatusCode.OK -> {
                        response.body<ServiceEdit>().also {
                            println("Successfully loaded profile: $it")
                        } ?: throw Exception("Empty response body")
                    }
                    HttpStatusCode.NotFound -> throw Exception("not found")
                    else -> throw Exception("Server error: ${response.status}")
                }
            } catch (e: Exception) {
                println("Ошибка получения: ${e.message}")
                throw e
            }
        }
    }

    suspend fun updateService(serviceId: Int, data: ServiceEdit): Boolean {
        val response = client.put("http://10.0.2.2:8080/updateService/$serviceId") {
            contentType(ContentType.Application.Json)
            setBody(data)
        }

        return response.status.isSuccess()
    }

    suspend fun deleteService(serviceId: Int) {
        withContext(Dispatchers.IO) {
            try {
                val response = client.delete("http://10.0.2.2:8080/deleteService/$serviceId") {
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