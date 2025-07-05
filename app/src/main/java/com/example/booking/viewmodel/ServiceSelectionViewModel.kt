package com.example.booking.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booking.data.NetworkService
import com.example.booking.models.Service
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ServiceSelectionViewModel : ViewModel() {
    private val networkService = NetworkService()

    private val _services = MutableStateFlow<List<Service>>(emptyList())
    val services: StateFlow<List<Service>> = _services

    private val _isLoading = MutableStateFlow(false)

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun loadServices(enterpriseId: Int?) {
        enterpriseId ?: return
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val servicesList = networkService.getEnterpriseServices(enterpriseId)
                _services.value = servicesList
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Ошибка загрузки"
            } finally {
                _isLoading.value = false
            }
        }
    }

}