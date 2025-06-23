package com.example.booking.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booking.data.NetworkClient
import com.example.booking.models.RegistrationData
import kotlinx.coroutines.launch

class RegistrationViewModel : ViewModel() {

    private val networkClient = NetworkClient()

    fun registerUser(fullName: String, phoneNumber: String, password: String, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val registrationData = RegistrationData(fullName, phoneNumber, password)
            try {
                val response = networkClient.registerUser(registrationData)
                onSuccess(response)
            } catch (e: Exception) {
                onError(e.localizedMessage ?: "Ошибка регистрации")
            }
        }
    }
}