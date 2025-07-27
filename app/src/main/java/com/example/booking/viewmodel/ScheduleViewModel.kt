package com.example.booking.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booking.data.NetworkSchedule
import com.example.booking.models.EmployeeScheduleResponse
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class ScheduleViewModel(
    private val scheduleService: NetworkSchedule = NetworkSchedule()
) : ViewModel() {
    var schedules by mutableStateOf<List<EmployeeScheduleResponse>>(emptyList())
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)
    var selectedDate by mutableStateOf(LocalDate.now())

    fun loadSchedules(date: LocalDate = selectedDate) {
        viewModelScope.launch {
            isLoading = true
            error = null
            selectedDate = date

            try {
                val formattedDate = date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                schedules = scheduleService.getDailySchedules(formattedDate)
            } catch (e: Exception) {
                error = "Ошибка загрузки расписания: ${e.message}"
                schedules = emptyList()
            } finally {
                isLoading = false
            }
        }
    }
}