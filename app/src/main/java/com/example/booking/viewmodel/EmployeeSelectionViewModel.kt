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


    fun loadEmployee(enterpriseId: Int) {
        viewModelScope.launch {
            try {
                _employee.value = networkEmployee.getEnterpriseEmployee(enterpriseId)
            } catch (e: Exception) {
                _employee.value = emptyList()
            }
        }
    }

}