package com.example.booking.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booking.data.*
import com.example.booking.models.*
import kotlinx.coroutines.launch

class EnterpriseRegistrationViewModel : ViewModel() {

    private val networkEnterprise = NetworkEnterprise()

    fun registerEnterprise(enterpriseName: String, city: String, address: String, enterprisePhoneNumber: String,
                           onSuccess: (Int) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val registrationData = EnterpriseRegistrationData(enterpriseName, city, address, enterprisePhoneNumber)
            try {
                val enterpriseId = networkEnterprise.registerEnterprise(registrationData)
                onSuccess(enterpriseId)
            } catch (e: Exception) {
                val errorMessage = e.localizedMessage ?: "Ошибка регистрации"
                onError(errorMessage)
            }
        }
    }

    fun backEnterprise(userId: Int) {
        viewModelScope.launch {
            networkEnterprise.backEnterprise(userId)
        }
    }
}