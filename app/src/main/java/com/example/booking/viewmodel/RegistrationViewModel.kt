package com.example.booking.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booking.data.NetworkClient
import com.example.booking.models.RegistrationData
import com.example.booking.models.UserData
import io.ktor.client.plugins.ResponseException
import io.ktor.http.HttpStatusCode
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

    fun loadUserProfile(userId: Int, onSuccess: (UserData) -> Unit) {
        viewModelScope.launch {
            try {
                val profile = networkClient.loadUserProfile(userId)
                onSuccess(
                    UserData(
                        fullName = profile.fullName,
                        phoneNumber = profile.phoneNumber,
                        password = profile.password
                    )
                )

            } catch (e: Exception) {
                 when (e) {
                    is ResponseException -> {
                        when (e.response.status) {
                            HttpStatusCode.NotFound -> "Пользователь не найден"
                            HttpStatusCode.Unauthorized -> "Требуется авторизация"
                            else -> "Ошибка сервера: ${e.response.status}"
                        }
                    }
                    else -> "Ошибка сети: ${e.localizedMessage ?: "Неизвестная ошибка"}"
                }
            }
        }
    }


    fun updateUserProfile(userId: Int?, fullName: String, phone: String, password: String?, onSuccess: () -> Unit
    ) {
            viewModelScope.launch {
                try {
                    if (userId == null) {
                        return@launch
                    }

                    val updateData = UserData(
                        fullName = fullName,
                        phoneNumber = phone,
                        password = password ?: ""
                    )

                    val isSuccess = networkClient.updateUserProfile(userId, updateData)

                    if (isSuccess) {
                        onSuccess()
                    }

                } catch (e: Exception) {
                    "Ошибка сети: ${e.localizedMessage ?: "Неизвестная ошибка"}"
                }
        }
    }
}