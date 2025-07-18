// Файл: NetworkWorkingHours.kt
package com.example.booking.data

import com.example.booking.models.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class NetworkWorkingHours {
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

    suspend fun saveWorkingHours(
        employeeId: Int,
        scheduleType: String,
        workTime: WorkTime,
        workPeriod: WorkPeriod,
        breaks: List<BreakTime>
    ): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val request = WorkingHoursRequest(
                    employeeId = employeeId,
                    scheduleType = scheduleType,
                    workTimeSlots = listOf(
                        WorkTimeSlotRequest(
                            startTime = workTime.start,
                            endTime = workTime.end,
                            validFrom = workPeriod.start,
                            validTo = workPeriod.end,
                            breaks = breaks.map {
                                BreakRequest(it.start, it.end)
                            }
                        )
                    )
                )

                val response = client.post("http://10.0.2.2:8080/employee/schedule") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }

                response.status == HttpStatusCode.OK
            } catch (e: Exception) {
                false
            }
        }
    }


    suspend fun saveWorkingWeeksHours(
        employeeId: Int,
        scheduleType: String,
        dayOfWeeks: String,
        scheduleSubType: String,
        workTime: WorkTime,
        workPeriod: WorkPeriod,
        breaks: List<BreakTime>
    ): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val request = WorkingWeeksHoursRequest(
                    employeeId = employeeId,
                    scheduleType = scheduleType,
                    dayOfWeek = dayOfWeeks,
                    scheduleSubType = scheduleSubType,
                    workTimeSlots = listOf(
                        WorkTimeSlotRequest(
                            startTime = workTime.start,
                            endTime = workTime.end,
                            validFrom = workPeriod.start,
                            validTo = workPeriod.end,
                            breaks = breaks.map {
                                BreakRequest(it.start, it.end)
                            }
                        )
                    )
                )

                val response = client.post("http://10.0.2.2:8080/employee/week-schedule") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }

                response.status == HttpStatusCode.OK
            } catch (e: Exception) {
                false
            }
        }
    }

    suspend fun getWorkingHours(employeeId: Int, scheduleType: String?): List<WorkingHoursResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val url = buildString {
                    append("http://10.0.2.2:8080/employee/$employeeId/schedule")
                    if (scheduleType != null) {
                        append("?type=$scheduleType")
                    }
                }
                client.get(url).body()
            } catch (e: Exception) {
                emptyList()
            }
        }
    }


    suspend fun getWorkingWeeksHours(
        employeeId: Int,
        scheduleType: String? = null,
        dayOfWeeks: String? = null,
        scheduleSubType: String? = null
    ): List<WorkingWeeksHoursResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val url = buildString {
                    append("http://10.0.2.2:8080/employee/$employeeId/week-schedule")
                    val params = mutableListOf<String>()

                    scheduleType?.let {
                        params.add("type=$it")
                    }

                    dayOfWeeks?.let {
                        params.add("days=$it")
                    }

                    scheduleSubType?.let {
                        params.add("subType=$it")
                    }

                    if (params.isNotEmpty()) {
                        append("?")
                        append(params.joinToString("&"))
                    }
                }

                client.get(url).body()
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    suspend fun getWorkingChoiceHours(
        employeeId: Int,
        scheduleType: String? = null,
        dayWork: String? = null,
        dayRest: String? = null,
        scheduleSubType: String? = null
    ): List<WorkingChoiceHoursResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val url = buildString {
                    append("http://10.0.2.2:8080/employee/$employeeId/choice-schedule")
                    val params = mutableListOf<String>()

                    scheduleType?.let {
                        params.add("type=$it")
                    }

                    dayWork?.let {
                            params.add("dayWork=$it")
                    }

                    dayRest?.let {
                        params.add("dayRest=$it")
                    }

                    scheduleSubType?.let {
                        params.add("subType=$it")
                    }

                    if (params.isNotEmpty()) {
                        append("?")
                        append(params.joinToString("&"))
                    }
                }

                client.get(url).body()
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    suspend fun saveWorkingChoiceHours(
        employeeId: Int,
        scheduleType: String,
        dayWork: String,
        dayRest: String,
        scheduleSubType: String,
        workTime: WorkTime,
        workPeriod: WorkPeriod,
        breaks: List<BreakTime>
    ): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val request = WorkingChoiceHoursRequest(
                    employeeId = employeeId,
                    scheduleType = scheduleType,
                    dayWork = dayWork,
                    dayRest = dayRest,
                    scheduleSubType = scheduleSubType,
                    workTimeSlots = listOf(
                        WorkTimeSlotRequest(
                            startTime = workTime.start,
                            endTime = workTime.end,
                            validFrom = workPeriod.start,
                            validTo = workPeriod.end,
                            breaks = breaks.map {
                                BreakRequest(it.start, it.end)
                            }
                        )
                    )
                )

                val response = client.post("http://10.0.2.2:8080/employee/choice-schedule") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }

                response.status == HttpStatusCode.OK
            } catch (e: Exception) {
                false
            }
        }
    }
}