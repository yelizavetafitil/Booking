package com.example.booking.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booking.data.*
import com.example.booking.models.*
import io.ktor.client.plugins.ResponseException
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.launch

class EmployeeViewModel : ViewModel() {

    private val networkEmployee = NetworkEmployee()

    var employee_fio by mutableStateOf("")
    var employee_phone by mutableStateOf("")
    var position by mutableStateOf("")
    var currentAccess by mutableStateOf("")

    fun updateAccess(newAccess: String) {
        println("Updating access to: $newAccess")
        currentAccess = newAccess
    }

    fun resetForm() {
        employee_fio = ""
        employee_phone = ""
        position = ""
        currentAccess = ""
    }

    fun addEmployee(
        enterpriseId: Int,
        employee_fio: String,
        employee_phone: String,
        position: String,
        access: String,
        onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val Data = EmployeeData(enterpriseId, employee_fio, employee_phone, position, access)
            try {
                networkEmployee.addEmployee(Data)
                onSuccess()
            } catch (e: Exception) {
                val errorMessage = e.localizedMessage ?: "Ошибка регистрации"
                onError(errorMessage)
            }
        }
    }


    fun loadEmployee(employeeId: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val employee = networkEmployee.loadEmployee(employeeId)
                employee_fio = employee.employee_fio
                employee_phone = employee.employee_phone
                position = employee.position
                currentAccess  = employee.access
                onSuccess()
            } catch (e: Exception) {
                when (e) {
                    is ResponseException -> {
                        when (e.response.status) {
                            HttpStatusCode.NotFound -> "не найден"
                            else -> "Ошибка сервера: ${e.response.status}"
                        }
                    }
                    else -> "Ошибка сети: ${e.localizedMessage ?: "Неизвестная ошибка"}"
                }
            }
        }
    }

    fun updateEmployee(
        employeeId: Int,
        employee_fio: String,
        employee_phone: String,
        position: String,
        access: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                if (employeeId == null) {
                    return@launch
                }

                val Data = EmployeeEdit(
                    employee_fio = employee_fio,
                    employee_phone = employee_phone,
                    position = position,
                    access = access,
                )

                val isSuccess = networkEmployee.updateEmployee(employeeId, Data)

                if (isSuccess) {
                    onSuccess()
                }

            } catch (e: Exception) {
                "Ошибка сети: ${e.localizedMessage ?: "Неизвестная ошибка"}"
            }
        }
    }

    fun deleteEmployee(employeeId: Int , onSuccess: () -> Unit) {
        viewModelScope.launch {
            networkEmployee.deleteEmployee(employeeId)
            onSuccess()
        }
    }

}