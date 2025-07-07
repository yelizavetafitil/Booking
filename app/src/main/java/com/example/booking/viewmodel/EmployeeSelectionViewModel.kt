package com.example.booking.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booking.data.NetworkEmployee
import com.example.booking.data.NetworkService
import com.example.booking.models.Employee
import com.example.booking.models.Service
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EmployeeSelectionViewModel : ViewModel() {
    private val networkEmployee = NetworkEmployee()

    private val _employee = MutableStateFlow<List<Employee>>(emptyList())
    val employee: StateFlow<List<Employee>> = _employee

    private val _isLoading = MutableStateFlow(false)

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun loadEmployee(enterpriseId: Int?) {
        enterpriseId ?: return
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val employeeList = networkEmployee.getEnterpriseEmployee(enterpriseId)
                _employee.value = employeeList
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Ошибка загрузки"
            } finally {
                _isLoading.value = false
            }
        }
    }

}