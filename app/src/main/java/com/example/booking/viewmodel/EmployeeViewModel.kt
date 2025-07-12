package com.example.booking.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booking.data.*
import com.example.booking.models.*
import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.bodyAsText
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


    private val _currentEmployees = mutableStateOf<Set<Int>>(emptySet())
    val currentEmployees: Set<Int> get() = _currentEmployees.value

    var isLoading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    fun loadCurrentEmployees(serviceId: Int) {
        viewModelScope.launch {
            isLoading = true
            error = null
            try {
                _currentEmployees.value = networkEmployee
                    .getCurrentServiceEmployees(serviceId)
                    .toSet()
            } catch (e: Exception) {
                error = e.localizedMessage ?: "Failed to load employees"
            } finally {
                isLoading = false
            }
        }
    }

    fun clearError() {
        error = null
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


    fun EmployeesToService(
        serviceId: Int,
        employeeIds: List<Int>,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                val request = EmployeesToService(
                    service_id = serviceId,
                    employee_ids = employeeIds
                )

                val isSuccess = networkEmployee.EmployeesToService(request)

                if (isSuccess) {
                    onSuccess()
                } else {
                    Log.e("EmployeesToService", "Operation failed")
                }
            } catch (e: Exception) {
                val errorMessage = when (e) {
                    is ResponseException -> {
                        when (e.response.status) {
                            HttpStatusCode.BadRequest -> "Некорректный запрос: ${e.response.bodyAsText()}"
                            HttpStatusCode.NotFound -> "Услуга или сотрудники не найдены"
                            else -> "Ошибка сервера: ${e.response.status}"
                        }
                    }
                    is IllegalArgumentException -> e.message ?: "Ошибка валидации"
                    else -> "Ошибка сети: ${e.localizedMessage ?: "Неизвестная ошибка"}"
                }
                Log.e("EmployeesToService", errorMessage)
            }
        }
    }

    fun updateServiceEmployees(
        serviceId: Int,
        employeeIds: List<Int>,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            isLoading = true
            error = null
            try {
                val success = networkEmployee.updateServiceEmployees(serviceId, employeeIds)
                if (success) {
                    onSuccess()
                } else {
                    error = "Operation failed"
                }
            } catch (e: Exception) {
                error = e.localizedMessage ?: "Failed to update employees"
            } finally {
                isLoading = false
            }
        }
    }

}