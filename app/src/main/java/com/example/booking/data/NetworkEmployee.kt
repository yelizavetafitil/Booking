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


class NetworkEmployee {
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

    suspend fun addEmployee(data: EmployeeData) {
        withContext(Dispatchers.IO) {
            try {
                val response = client.post("http://10.0.2.2:8080/addEmployee") {
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

    suspend fun getEnterpriseEmployee(enterpriseId: Int): List<Employee> {
        return withContext(Dispatchers.IO) {
            try {
                val response = client.get("http://10.0.2.2:8080/enterpriseEmployee/$enterpriseId") {
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

    suspend fun getEnterpriseEmployeeService(enterpriseId: Int, serviceId: Int? = null): List<Employee> {
        return withContext(Dispatchers.IO) {
            try {
                val url = buildString {
                    append("http://10.0.2.2:8080/enterpriseEmployeeService/$enterpriseId")
                    if (serviceId != null) {
                        append("?serviceId=$serviceId")
                    }
                }

                val response = client.get(url) {
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

    suspend fun loadEmployee(employeeId: Int): EmployeeEdit {
        return withContext(Dispatchers.IO) {
            try {
                val response = client.get("http://10.0.2.2:8080/employees/$employeeId") {
                    accept(ContentType.Application.Json)
                }

                when (response.status) {
                    HttpStatusCode.OK -> {
                        response.body<EmployeeEdit>().also {
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

    suspend fun updateEmployee(employeeId: Int, data: EmployeeEdit): Boolean {
        val response = client.put("http://10.0.2.2:8080/updateEmployee/$employeeId") {
            contentType(ContentType.Application.Json)
            setBody(data)
        }

        return response.status.isSuccess()
    }

    suspend fun deleteEmployee(employeeId: Int) {
        withContext(Dispatchers.IO) {
            try {
                val response = client.delete("http://10.0.2.2:8080/deleteEmployee/$employeeId") {
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

    suspend fun EmployeesToService(request: EmployeesToService): Boolean {
        return try {
            val response = client.post("http://10.0.2.2:8080/service-employees") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }

            when (response.status) {
                HttpStatusCode.OK -> true
                HttpStatusCode.BadRequest -> throw IllegalArgumentException(response.bodyAsText())
                else -> false
            }
        } catch (e: Exception) {
            throw when (e) {
                is IllegalArgumentException -> e
                else -> Exception("Network error: ${e.localizedMessage}")
            }
        }
    }

    suspend fun getCurrentServiceEmployees(serviceId: Int): List<Int> {
        return try {
            val response = client.get("http://10.0.2.2:8080/service-employees/$serviceId") {
                contentType(ContentType.Application.Json)
            }

            when (response.status) {
                HttpStatusCode.OK -> response.body<List<Int>>()
                HttpStatusCode.NotFound -> emptyList()
                else -> throw Exception("Server error: ${response.status}")
            }
        } catch (e: Exception) {
            throw Exception("Failed to load employees: ${e.localizedMessage}")
        }
    }

    suspend fun updateServiceEmployees(serviceId: Int, employeeIds: List<Int>): Boolean {
        return try {
            val response = client.put("http://10.0.2.2:8080/service-employees/$serviceId") {
                contentType(ContentType.Application.Json)
                setBody(employeeIds)
            }

            when (response.status) {
                HttpStatusCode.OK -> true
                HttpStatusCode.BadRequest -> throw IllegalArgumentException(response.bodyAsText())
                else -> false
            }
        } catch (e: Exception) {
            throw when (e) {
                is IllegalArgumentException -> e
                else -> Exception("Network error: ${e.localizedMessage}")
            }
        }
    }

}