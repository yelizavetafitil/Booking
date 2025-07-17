
package com.example.booking.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booking.data.NetworkWorkingHours
import com.example.booking.models.*
import kotlinx.coroutines.launch
import java.io.IOException

class WorkingHoursViewModel(
    private val networkWorkingHours: NetworkWorkingHours = NetworkWorkingHours()
) : ViewModel() {

    var workingHours by mutableStateOf<WorkingHoursData?>(null)
    var workingWeeksHours by mutableStateOf<WorkingWeeksHoursData?>(null)
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)
    var currentScheduleType by mutableStateOf<String?>(null)

    fun loadWorkingHours(employeeId: Int, scheduleType: String?) {
        viewModelScope.launch {
            isLoading = true
            error = null
            try {
                val responses = networkWorkingHours.getWorkingHours(employeeId, scheduleType)
                if (responses.isNotEmpty()) {
                    val firstSchedule = responses[0]
                    if (firstSchedule.workTimeSlots.isNotEmpty()) {
                        val firstSlot = firstSchedule.workTimeSlots[0]

                        workingHours = WorkingHoursData(
                            scheduleType = firstSchedule.scheduleType,
                            workTime = WorkTime(firstSlot.startTime, firstSlot.endTime),
                            period = WorkPeriod(firstSlot.validFrom, firstSlot.validTo),
                            breaks = firstSlot.breaks.map {
                                BreakTime(it.startTime, it.endTime)
                            }
                        )
                    }
                }
            } catch (e: Exception) {
                error = "Ошибка загрузки: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun loadWorkingWeeksHours(
        employeeId: Int,
        scheduleType: String?,
        dayOfWeeks: String?,
        scheduleSubType: String?
    ) {
        viewModelScope.launch {
            isLoading = true
            error = null
            try {
                val response = networkWorkingHours.getWorkingWeeksHours(
                    employeeId,
                    scheduleType,
                    dayOfWeeks,
                    scheduleSubType
                )

                if (response.isNotEmpty()) {
                    val firstSchedule = response[0]
                    if (firstSchedule.workTimeSlots.isNotEmpty()) {
                        val slot = firstSchedule.workTimeSlots[0]
                        workingWeeksHours = WorkingWeeksHoursData(
                            scheduleType = firstSchedule.scheduleType,
                            dayOfWeek = firstSchedule.dayOfWeek,
                            scheduleSubType = firstSchedule.scheduleSubType,
                            workTime = WorkTime(slot.startTime, slot.endTime),
                            period = WorkPeriod(slot.validFrom, slot.validTo),
                            breaks = slot.breaks.map {
                                BreakTime(it.startTime, it.endTime)
                            }
                        )
                    }
                }
            } catch (e: Exception) {
                error = "Ошибка загрузки: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun saveWorkingHours(
        employeeId: Int,
        scheduleType: String,
        workTime: WorkTime,
        workPeriod: WorkPeriod,
        breaks: List<BreakTime>,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            isLoading = true
            error = null

            try {
                val response = networkWorkingHours.saveWorkingHours(
                    employeeId = employeeId,
                    scheduleType = scheduleType,
                    workTime = workTime,
                    workPeriod = workPeriod,
                    breaks = breaks
                )

                if (response) {
                    workingHours = WorkingHoursData(
                        scheduleType,
                        workTime,
                        workPeriod,
                        breaks
                    )
                    currentScheduleType = scheduleType
                    onSuccess()
                } else {
                    error ="Не удалось сохранить график, проверьте формат," +
                            " перерыв должен быть в пределах рабочего времени"
                }
            } catch (e: IOException) {
                error = "Ошибка сети: проверьте подключение"
            } catch (e: Exception) {
                error = "Ошибка: ${e.localizedMessage ?: "неизвестная ошибка"}"
            } finally {
                isLoading = false
            }
        }
    }

    fun saveWorkingWeeksHours(
        employeeId: Int,
        scheduleType: String,
        dayOfWeeks: String,
        scheduleSubType: String,
        workTime: WorkTime,
        workPeriod: WorkPeriod,
        breaks: List<BreakTime>,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            isLoading = true
            error = null
            try {
                val success = networkWorkingHours.saveWorkingWeeksHours(
                    employeeId = employeeId,
                    scheduleType = scheduleType,
                    dayOfWeeks = dayOfWeeks,
                    scheduleSubType = scheduleSubType,
                    workTime = workTime,
                    workPeriod = workPeriod,
                    breaks = breaks
                )

                if (success) {
                    workingWeeksHours = WorkingWeeksHoursData(
                        scheduleType,
                        dayOfWeeks,
                        scheduleSubType,
                        workTime,
                        workPeriod,
                        breaks
                    )
                    currentScheduleType = scheduleType
                    onSuccess()
                } else {
                    error ="Не удалось сохранить график, проверьте формат," +
                            " перерыв должен быть в пределах рабочего времени"
                }
            } catch (e: Exception) {
                error = "Ошибка сохранения: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun clearError() {
        error = null
    }
}