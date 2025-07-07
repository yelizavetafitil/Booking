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
import com.example.booking.R
import com.example.booking.viewmodel.EmployeeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeEditScreen(
    viewModel: EmployeeViewModel,
    userId: Int?,
    enterpriseId: Int?,
    employeeId: Int?,
    onBackClick: (userId: Int?, enterpriseId: Int) -> Unit,
    onSaveClick: (userId: Int?, enterpriseId: Int) -> Unit,
    onAccessClick: (access: String) -> Unit,
    onDeleteClick: (userId: Int?, enterpriseId: Int?) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()
    var showDeleteDialog by remember { mutableStateOf(false) }


    val customFontFamily = FontFamily(
        Font(R.font.roboto_regular),
        Font(R.font.roboto_bold, FontWeight.Bold)
    )

    println("Now access: ${viewModel.currentAccess}")

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
                    text = "Редактировать сотрудника",
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
                        value = viewModel.employee_fio,
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

                    val disabledFieldColor = Color(0xFFF5F5F5)

                    OutlinedTextField(
                        value = viewModel.employee_phone,
                        onValueChange = {},
                        label = {
                            Text(
                                "Номер телефона",
                                fontFamily = customFontFamily,
                                color = Color.Gray
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = disabledFieldColor,
                            unfocusedContainerColor = disabledFieldColor,
                            focusedIndicatorColor = Color.Gray,
                            unfocusedIndicatorColor = Color.Gray,
                            focusedLabelColor = Color.Gray,
                            unfocusedLabelColor = Color.Gray,
                            disabledContainerColor = disabledFieldColor,
                            disabledIndicatorColor = Color.Gray,
                            disabledTextColor = Color.Black,
                            disabledLabelColor = Color.Gray
                        ),
                        enabled = false,
                        singleLine = true,
                        textStyle = TextStyle(
                            fontFamily = customFontFamily,
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                    )

                    OutlinedTextField(
                        value = viewModel.position,
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
                                        fontWeight = FontWeight.Bold
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
                        shape = RoundedCornerShape(24.dp),
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
                                        if (userId != null && enterpriseId != null && employeeId != null) {
                                            viewModel.updateEmployee(
                                                employeeId = employeeId,
                                                employee_fio = viewModel.employee_fio,
                                                employee_phone = viewModel.employee_phone,
                                                position = viewModel.position,
                                                access = viewModel.currentAccess,
                                                onSuccess = {
                                                    onSaveClick(userId, enterpriseId)
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

                    Button(
                        onClick = {
                            showDeleteDialog=true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black,
                            contentColor = Color.Red
                        )
                    ) {
                        Text(
                            "Удалить",
                            fontFamily = customFontFamily,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    if (showDeleteDialog) {
                        AlertDialog(
                            onDismissRequest = { showDeleteDialog = false },
                            title = {
                                Text(
                                    "Подтверждение удаления",
                                    style = TextStyle(
                                        fontFamily = customFontFamily,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            },
                            text = {
                                Text(
                                    "Вы уверены, что хотите удалить сотрудника?",
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        fontFamily = customFontFamily
                                    )
                                )
                            },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        showDeleteDialog = false
                                        if(employeeId==null||userId==null||enterpriseId==null){
                                            return@Button
                                        }
                                        viewModel.deleteEmployee(employeeId, onSuccess = {
                                            onDeleteClick(userId, enterpriseId)
                                        })
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.Black
                                    )
                                ) {
                                    Text("Да", color = Color.White)
                                }
                            },
                            dismissButton = {
                                OutlinedButton(
                                    onClick = { showDeleteDialog = false }
                                ) {
                                    Text("Нет", color = Color.Black)
                                }
                            },
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .padding(horizontal = 24.dp),
                            containerColor = Color.White,
                            tonalElevation = 0.dp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}