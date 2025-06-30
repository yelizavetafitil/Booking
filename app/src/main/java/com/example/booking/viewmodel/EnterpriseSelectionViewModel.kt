package com.example.booking.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booking.data.NetworkEnterprise
import com.example.booking.models.EnterpriseData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EnterpriseSelectionViewModel : ViewModel() {
    private val networkEnterprise = NetworkEnterprise()

    private val _enterprises = MutableStateFlow<List<EnterpriseData>>(emptyList())
    val enterprises: StateFlow<List<EnterpriseData>> = _enterprises

    private val _isLoading = MutableStateFlow(false)

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun loadEnterprises(userId: Int?) {
        userId ?: return
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val enterprisesList = networkEnterprise.getUserEnterprises(userId)
                _enterprises.value = enterprisesList
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Ошибка загрузки предприятий"
            } finally {
                _isLoading.value = false
            }
        }
    }

}