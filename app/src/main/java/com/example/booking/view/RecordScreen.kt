package com.example.booking.view

import android.os.Build
import androidx.annotation.RequiresApi
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
import com.example.booking.viewmodel.RecordViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordScreen(
    userId: Int?,
    enterpriseId: Int,
    serviceId: Int,
    employeeId: Int,
    selectedTime: String,
    selectedDate: LocalDate,
    onNext: (userId: Int?, enterpriseId: Int) -> Unit,
    onBackClick: (userId: Int?, enterpriseId: Int, serviceId: Int, employeeId: Int) -> Unit
) {
    val viewModel: RecordViewModel = viewModel()
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    val customFontFamily = FontFamily(
        Font(R.font.roboto_regular),
        Font(R.font.roboto_bold, FontWeight.Bold)
    )

    val fullName by remember { derivedStateOf { viewModel.fullName } }
    val phone by remember { derivedStateOf { viewModel.phone } }
    val comment by remember { derivedStateOf { viewModel.comment } }

    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    
    LaunchedEffect(serviceId, employeeId) {
        viewModel.loadServiceAndEmployeeNames(serviceId, employeeId)
    }

    val dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", java.util.Locale("ru"))
    val formattedDate = selectedDate.format(dateFormatter)

    val formatter = DateTimeFormatter.ofPattern("dd.MM.yy", Locale.getDefault())
    val date = selectedDate.format(formatter)

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
                        if (userId != null && enterpriseId != null && serviceId != null && employeeId != null) {
                            onBackClick(userId, enterpriseId, serviceId, employeeId)
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
                    text = "Подтверждение записи",
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
                when {
                    viewModel.isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Color.Black)
                        }
                    }
                    viewModel.error != null -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = viewModel.error!!,
                                style = TextStyle(
                                    fontFamily = customFontFamily,
                                    color = Color.Red
                                )
                            )
                        }
                    }
                    else -> {
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 24.dp)
                                .padding(top = 32.dp)
                                .verticalScroll(scrollState),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFFF5F5F5)
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Text(
                                        text = "Детали записи",
                                        style = TextStyle(
                                            fontFamily = customFontFamily,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp
                                        ),
                                        modifier = Modifier.padding(bottom = 12.dp)
                                    )

                                    InfoRow(
                                        label = "Услуга:",
                                        value = viewModel.serviceName,
                                        fontFamily = customFontFamily
                                    )

                                    InfoRow(
                                        label = "Специалист:",
                                        value = viewModel.employeeName,
                                        fontFamily = customFontFamily
                                    )

                                    InfoRow(
                                        label = "Дата:",
                                        value = formattedDate,
                                        fontFamily = customFontFamily
                                    )

                                    InfoRow(
                                        label = "Время:",
                                        value = selectedTime,
                                        fontFamily = customFontFamily
                                    )
                                }
                            }

                            Text(
                                text = "Ваши данные",
                                style = TextStyle(
                                    fontFamily = customFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            )

                            
                            OutlinedTextField(
                                value = fullName,
                                onValueChange = { viewModel.fullName = it },
                                label = {
                                    Text(
                                        "ФИО",
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

                            OutlinedTextField(
                                value = phone,
                                onValueChange = { viewModel.phone = it },
                                label = { Text("Номер телефона", fontFamily = customFontFamily) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
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
                                textStyle = TextStyle(fontFamily = customFontFamily, fontSize = 16.sp)
                            )

                            
                            OutlinedTextField(
                                value = comment,
                                onValueChange = { viewModel.comment = it },
                                label = {
                                    Text(
                                        "Комментарий (необязательно)",
                                        fontFamily = customFontFamily
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White,
                                    focusedIndicatorColor = Color.Black,
                                    unfocusedIndicatorColor = Color.Gray,
                                    focusedLabelColor = Color.Black,
                                    unfocusedLabelColor = Color.Gray
                                ),
                                maxLines = 4,
                                textStyle = TextStyle(fontFamily = customFontFamily),
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                keyboardActions = KeyboardActions(
                                    onDone = { focusManager.clearFocus() }
                                )
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            
                            Button(
                                onClick = {
                                    when {
                                        viewModel.fullName.isBlank() -> {
                                            errorMessage = "Введите ФИО"
                                            showErrorDialog = true
                                        }
                                        viewModel.phone.isBlank() -> {
                                            errorMessage = "Введите телефон"
                                            showErrorDialog = true
                                        }
                                        else -> {
                                            try {
                                                if (userId != null && enterpriseId != null) {
                                                    viewModel.addRecord(
                                                        enterpriseId = enterpriseId,
                                                        serviceId = serviceId,
                                                        employeeId = employeeId,
                                                        date = date.toString(),
                                                        time = selectedTime,
                                                        fullName = viewModel.fullName,
                                                        phone = viewModel.phone,
                                                        comment = viewModel.comment,
                                                        onSuccess = {
                                                            onNext(userId, enterpriseId)
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
                                ),
                                enabled = fullName.isNotBlank() && phone.isNotBlank()
                            ) {
                                Text(
                                    "Подтвердить запись",
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
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String,
    fontFamily: FontFamily
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = TextStyle(
                fontFamily = fontFamily,
                color = Color.Gray,
                fontSize = 14.sp
            )
        )
        Text(
            text = value,
            style = TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        )
    }
}