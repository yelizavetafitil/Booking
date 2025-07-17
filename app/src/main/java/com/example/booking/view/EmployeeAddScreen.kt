package com.example.booking.view

import androidx.compose.foundation.clickable
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
import com.example.booking.viewmodel.EmployeeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeAddScreen(
    viewModel: EmployeeViewModel,
    userId: Int?,
    enterpriseId: Int?,
    onBackClick: (userId: Int?, enterpriseId: Int) -> Unit,
    onSaveClick: (userId: Int?, enterpriseId: Int) -> Unit,
    onAccessClick: (access: String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    val employeeFio by remember { derivedStateOf { viewModel.employee_fio } }
    val employeePhone by remember { derivedStateOf { viewModel.employee_phone } }
    val position by remember { derivedStateOf { viewModel.position } }

    val customFontFamily = FontFamily(
        Font(R.font.roboto_regular),
        Font(R.font.roboto_bold, FontWeight.Bold)
    )

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
                    text = "Добавить сотрудника",
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
                        value = employeeFio,
                        onValueChange = { viewModel.employee_fio = it },
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
                        value = employeePhone,
                        onValueChange = { viewModel.employee_phone = it },
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
                        value = position,
                        onValueChange = { viewModel.position = it },
                        label = {
                            Text(
                                "Должность",
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

                    if (viewModel.currentAccess.isNotEmpty()) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onAccessClick(viewModel.currentAccess) },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.Black,
                                contentColor = Color.White
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "Текущий доступ: ${viewModel.currentAccess}",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Medium
                                    )
                                )
                            }
                        }
                    }

                    Button(
                        onClick = {
                            onAccessClick(viewModel.currentAccess)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black,
                            contentColor = Color.White
                        ),
                        elevation = ButtonDefaults.buttonElevation(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Выбрать доступ",
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontFamily = customFontFamily,
                                    fontWeight = FontWeight.Medium,
                                    letterSpacing = 0.8.sp
                                )
                            )
                            Icon(
                                painter = painterResource(id = R.drawable.ic_arrow),
                                contentDescription = "Стрелка",
                                tint = Color.Unspecified,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = {
                            when {
                                viewModel.employee_fio.isBlank() -> {
                                    errorMessage = "Введите ФИО"
                                    showErrorDialog = true
                                }
                                viewModel.employee_phone.isBlank() -> {
                                    errorMessage = "Введите телефон"
                                    showErrorDialog = true
                                }
                                viewModel.position.isBlank() -> {
                                    errorMessage = "Введите должность"
                                    showErrorDialog = true
                                }
                                else -> {
                                    try {
                                        if (userId != null && enterpriseId != null) {
                                            viewModel.addEmployee(
                                                enterpriseId = enterpriseId,
                                                employee_fio = viewModel.employee_fio,
                                                employee_phone = viewModel.employee_phone,
                                                position = viewModel.position,
                                                access = viewModel.currentAccess,
                                                onSuccess = {
                                                    onSaveClick(userId, enterpriseId)
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