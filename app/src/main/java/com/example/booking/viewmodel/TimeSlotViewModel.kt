package com.example.booking.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booking.data.NetworkRecord
import com.example.booking.data.NetworkService
import kotlinx.coroutines.launch
import java.time.LocalDate

class TimeSlotViewModel : ViewModel() {
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)
    var timeSlots by mutableStateOf<Map<String, List<String>>>(emptyMap())

    private val networkRecord = NetworkRecord()

    fun loadTimeSlots(employeeId: Int, serviceId: Int, date: LocalDate) {
        viewModelScope.launch {
            isLoading = true
            error = null

            try {
                val slots = networkRecord.getTimeSlots(employeeId, serviceId, date)
                timeSlots = slots
            } catch (e: Exception) {
                error = e.message ?: "Failed to load time slots"
                timeSlots = emptyMap()
            } finally {
                isLoading = false
            }
        }
    }
}