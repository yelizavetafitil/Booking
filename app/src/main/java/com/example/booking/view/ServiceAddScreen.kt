package com.example.booking.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.booking.R
import com.example.booking.viewmodel.ServiceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceAddScreen(
    userId: Int?,
    enterpriseId: Int?,
    onBackClick: (userId: Int?, enterpriseId: Int) -> Unit,
    onSaveClick: (userId: Int?, enterpriseId: Int, serviceId: Int) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()

    val customFontFamily = FontFamily(
        Font(R.font.roboto_regular),
        Font(R.font.roboto_bold, FontWeight.Bold)
    )

    val viewModel: ServiceViewModel = viewModel()

    var serviceName by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var breakDuration by remember { mutableStateOf("") }

    val currencies = listOf("BYN", "RUB", "USD", "EUR")
    var selectedCurrency by remember { mutableStateOf(currencies[0]) }
    var expanded by remember { mutableStateOf(false) }

    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = {
                Text(
                    "Ошибка",
                    style = TextStyle(
                        fontFamily = customFontFamily,
                        fontWeight = FontWeight.Bold
                    )
                )
            },
            text = {
                Text(
                    errorMessage,
                    style = TextStyle(fontFamily = customFontFamily)
                )
            },
            confirmButton = {
                Button(
                    onClick = { showErrorDialog = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black
                    )
                ) {
                    Text(
                        "OK",
                        style = TextStyle(
                            fontFamily = customFontFamily,
                            color = Color.White
                        )
                    )
                }
            }
        )
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 56.dp, bottom = 32.dp)
            ) {
                IconButton(
                    onClick = {
                        if (userId != null && enterpriseId != null) {
                            onBackClick(userId, enterpriseId)
                        }
                              },
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 16.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_close),
                        contentDescription = "Закрыть",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Text(
                    text = "Добавить услугу",
                    style = TextStyle(
                        fontSize = 22.sp,
                        fontFamily = customFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        letterSpacing = 0.5.sp
                    ),
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .padding(top = 32.dp)
                        .verticalScroll(scrollState),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = serviceName,
                        onValueChange = { serviceName = it },
                        label = {
                            Text(
                                "Название",
                                fontFamily = customFontFamily
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedIndicatorColor = Color.Black,
                            unfocusedIndicatorColor = Color.Gray,
                            focusedLabelColor = Color.Black,
                            unfocusedLabelColor = Color.Gray
                        ),
                        singleLine = true,
                        textStyle = TextStyle(fontFamily = customFontFamily),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        )
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = price,
                            onValueChange = {
                                if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d{0,2}\$"))) {
                                    price = it
                                }
                            },
                            label = {
                                Text(
                                    "Стоимость",
                                    fontFamily = customFontFamily
                                )
                            },
                            modifier = Modifier.weight(2f),
                            shape = RoundedCornerShape(12.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedIndicatorColor = Color.Black,
                                unfocusedIndicatorColor = Color.Gray,
                                focusedLabelColor = Color.Black,
                                unfocusedLabelColor = Color.Gray
                            ),
                            singleLine = true,
                            textStyle = TextStyle(fontFamily = customFontFamily),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            )
                        )

                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = it },
                            modifier = Modifier.weight(1f)
                        ) {
                            OutlinedTextField(
                                value = selectedCurrency,
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                                },
                                label = {
                                    Text(
                                        "Валюта",
                                        fontFamily = customFontFamily
                                    )
                                },
                                modifier = Modifier.menuAnchor(),
                                shape = RoundedCornerShape(12.dp),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White,
                                    focusedIndicatorColor = Color.Black,
                                    unfocusedIndicatorColor = Color.Gray,
                                    focusedLabelColor = Color.Black,
                                    unfocusedLabelColor = Color.Gray
                                ),
                                textStyle = TextStyle(fontFamily = customFontFamily)
                            )

                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                currencies.forEach { currency ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                currency,
                                                fontFamily = customFontFamily
                                            )
                                        },
                                        onClick = {
                                            selectedCurrency = currency
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    OutlinedTextField(
                        value = duration,
                        onValueChange = {
                            if (it.isEmpty() || it.matches(Regex("^\\d+\$"))) {
                                duration = it
                            }
                        },
                        label = {
                            Text(
                                "Продолжительность (минуты)",
                                fontFamily = customFontFamily
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedIndicatorColor = Color.Black,
                            unfocusedIndicatorColor = Color.Gray,
                            focusedLabelColor = Color.Black,
                            unfocusedLabelColor = Color.Gray
                        ),
                        singleLine = true,
                        textStyle = TextStyle(fontFamily = customFontFamily),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        )
                    )

                    OutlinedTextField(
                        value = breakDuration,
                        onValueChange = {
                            if (it.isEmpty() || it.matches(Regex("^\\d+\$"))) {
                                breakDuration = it
                            }
                        },
                        label = {
                            Text(
                                "Перерыв после записи (минуты)",
                                fontFamily = customFontFamily
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedIndicatorColor = Color.Black,
                            unfocusedIndicatorColor = Color.Gray,
                            focusedLabelColor = Color.Black,
                            unfocusedLabelColor = Color.Gray
                        ),
                        singleLine = true,
                        textStyle = TextStyle(fontFamily = customFontFamily),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { keyboardController?.hide() }
                        )
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = {
                            when {
                                serviceName.isBlank() -> {
                                    errorMessage = "Введите название услуги"
                                    showErrorDialog = true
                                }
                                price.isBlank() -> {
                                    errorMessage = "Введите стоимость услуги"
                                    showErrorDialog = true
                                }
                                duration.isBlank() -> {
                                    errorMessage = "Введите продолжительность"
                                    showErrorDialog = true
                                }
                                else -> {
                                    try {
                                        if (userId != null && enterpriseId != null) {
                                            viewModel.addService(
                                                enterpriseId = enterpriseId,
                                                serviceName = serviceName,
                                                price = price.toDouble(),
                                                currency = selectedCurrency,
                                                length = duration.toInt(),
                                                breakDuration = breakDuration.toIntOrNull() ?: 0,
                                                onSuccess = { serviceId ->
                                                    onSaveClick(userId, enterpriseId, serviceId)
                                                },
                                                onError = { message ->
                                                    errorMessage = message
                                                    showErrorDialog = true
                                                }
                                            )
                                        }
                                    } catch (e: Exception) {
                                        errorMessage = "Ошибка: ${e.message}"
                                        showErrorDialog = true
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            "Сохранить",
                            fontFamily = customFontFamily,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}