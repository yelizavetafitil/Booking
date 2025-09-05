package com.example.booking.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booking.data.NetworkRecord
import com.example.booking.models.EmployeeData
import com.example.booking.models.RecordData
import kotlinx.coroutines.launch

class RecordViewModel : ViewModel() {
    private val networkRecord = NetworkRecord()

    var fullName by mutableStateOf("")
    var phone by mutableStateOf("")
    var comment by mutableStateOf("")

    var serviceName by mutableStateOf("")
    var employeeName by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)

    fun loadServiceAndEmployeeNames(serviceId: Int, employeeId: Int) {
        viewModelScope.launch {
            isLoading = true
            error = null
            try {
                
                serviceName = networkRecord.getServiceName(serviceId)

                
                employeeName = networkRecord.getEmployeeName(employeeId)
            } catch (e: Exception) {
                error = e.localizedMessage ?: "Failed to load data"
                
                serviceName = "Услуга #$serviceId"
                employeeName = "Сотрудник #$employeeId"
            } finally {
                isLoading = false
            }
        }
    }

    fun addRecord(
        enterpriseId: Int,
        serviceId: Int,
        employeeId: Int,
        date: String,
        time: String,
        fullName: String,
        phone: String,
        comment: String,
        onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val Data = RecordData(date, time, fullName, phone, comment, serviceId, employeeId, enterpriseId)
            try {
                networkRecord.addRecord(Data)
                onSuccess()
            } catch (e: Exception) {
                val errorMessage = e.localizedMessage ?: "Ошибка регистрации"
                onError(errorMessage)
            }
        }
    }

}