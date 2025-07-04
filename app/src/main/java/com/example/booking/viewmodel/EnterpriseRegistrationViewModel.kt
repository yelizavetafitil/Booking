package com.example.booking.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booking.data.*
import com.example.booking.models.*
import io.ktor.client.plugins.ResponseException
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.launch

class EnterpriseRegistrationViewModel : ViewModel() {

    private val networkEnterprise = NetworkEnterprise()

    fun registerEnterprise(enterpriseName: String, city: String, address: String, enterprisePhoneNumber: String, userId: Int,
                           onSuccess: (Int) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val registrationData = EnterpriseRegistrationData(enterpriseName, city, address, enterprisePhoneNumber, userId)
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

    fun loadEnterprise(enterpriseId: Int, onSuccess: (Enterprise) -> Unit) {
        viewModelScope.launch {
            try {
                val profile = networkEnterprise.loadEnterprise(enterpriseId)
                onSuccess(
                    Enterprise(
                        enterpriseName = profile.enterpriseName,
                        city = profile.city,
                        address = profile.address,
                        enterprisePhoneNumber = profile.enterprisePhoneNumber
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


    fun updateEnterprise(enterpriseId: Int?, enterpriseName: String, city: String, address: String?, enterprisePhoneNumber: String?,
                         onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                if (enterpriseId == null) {
                    return@launch
                }

                val Data = Enterprise(
                    enterpriseName = enterpriseName,
                    city = city,
                    address = address.toString(),
                    enterprisePhoneNumber = enterprisePhoneNumber.toString()
                )

                val isSuccess = networkEnterprise.updateEnterprise(enterpriseId, Data)

                if (isSuccess) {
                    onSuccess()
                }

            } catch (e: Exception) {
                "Ошибка сети: ${e.localizedMessage ?: "Неизвестная ошибка"}"
            }
        }
    }

    fun deleteEnterprise(enterpriseId: Int , onSuccess: () -> Unit) {
        viewModelScope.launch {
            networkEnterprise.deleteEnterprise(enterpriseId)
            onSuccess()
        }
    }

}