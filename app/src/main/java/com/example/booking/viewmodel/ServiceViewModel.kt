package com.example.booking.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booking.data.*
import com.example.booking.models.*
import io.ktor.client.plugins.ResponseException
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.launch

class ServiceViewModel : ViewModel() {

    private val networkService = NetworkService()

    fun addService(
         enterpriseId: Int,
         serviceName: String,
         price: Double,
         currency: String,
         length: Int,
         breakDuration: Int,
         onSuccess: (Int) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val Data = ServiceData(enterpriseId, serviceName, price, currency, length, breakDuration)
            try {
                val response = networkService.addService(Data)
                if (response.success) {
                    onSuccess(response.serviceId)
                }
            } catch (e: Exception) {
                val errorMessage = e.localizedMessage ?: "Ошибка регистрации"
                onError(errorMessage)
            }
        }
    }


    fun loadService(serviceId: Int, onSuccess: (ServiceEdit) -> Unit) {
        viewModelScope.launch {
            try {
                val service = networkService.loadService(serviceId)
                onSuccess(
                    ServiceEdit(
                        serviceName = service.serviceName,
                        price = service.price,
                        currency = service.currency,
                        length = service.length,
                        breakDuration = service.breakDuration
                    )
                )

            } catch (e: Exception) {
                when (e) {
                    is ResponseException -> {
                        when (e.response.status) {
                            HttpStatusCode.NotFound -> "не найден"
                            else -> "Ошибка сервера: ${e.response.status}"
                        }
                    }
                    else -> "Ошибка сети: ${e.localizedMessage ?: "Неизвестная ошибка"}"
                }
            }
        }
    }

    fun updateService(
        serviceId: Int,
        serviceName: String,
        price: Double,
        currency: String,
        length: Int,
        breakDuration: Int,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                if (serviceId == null) {
                    return@launch
                }

                val Data = ServiceEdit(
                    serviceName = serviceName,
                    price = price,
                    currency = currency,
                    length = length,
                    breakDuration = breakDuration
                )

                val isSuccess = networkService.updateService(serviceId, Data)

                if (isSuccess) {
                    onSuccess()
                }

            } catch (e: Exception) {
                "Ошибка сети: ${e.localizedMessage ?: "Неизвестная ошибка"}"
            }
        }
    }

    fun deleteService(serviceId: Int , onSuccess: () -> Unit) {
        viewModelScope.launch {
            networkService.deleteService(serviceId)
            onSuccess()
        }
    }

}