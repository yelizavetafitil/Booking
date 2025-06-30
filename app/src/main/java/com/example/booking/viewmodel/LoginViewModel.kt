package com.example.booking.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booking.data.NetworkLogin
import com.example.booking.models.RegistrationData
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val networkLogin = NetworkLogin()

    fun loginUser(fullName: String, phoneNumber: String, password: String, onSuccess: (Int) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val registrationData = RegistrationData(fullName, phoneNumber, password)
            try {
                val userId = networkLogin.loginUser(registrationData)
                onSuccess(userId)
            } catch (e: Exception) {
                val errorMessage = e.localizedMessage ?: "Ошибка регистрации"
                onError(errorMessage)
            }
        }
    }
}