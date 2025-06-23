package com.example.booking.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booking.data.NetworkClient
import com.example.booking.models.RegistrationData
import kotlinx.coroutines.launch

class RegistrationViewModel : ViewModel() {

    private val networkClient = NetworkClient()

    fun registerUser(fullName: String, phoneNumber: String, password: String, onSuccess: (Int) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val registrationData = RegistrationData(fullName, phoneNumber, password)
            try {
                val userId = networkClient.registerUser(registrationData)
                onSuccess(userId)
            } catch (e: Exception) {
                val errorMessage = e.localizedMessage ?: "Ошибка регистрации"
                onError(errorMessage)
            }
        }
    }
}