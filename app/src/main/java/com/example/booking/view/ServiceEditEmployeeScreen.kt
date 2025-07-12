package com.example.booking.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.booking.R
import com.example.booking.models.Employee
import com.example.booking.viewmodel.EmployeeSelectionViewModel
import com.example.booking.viewmodel.EmployeeViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceEditEmployeeScreen(
    userId: Int?,
    enterpriseId: Int?,
    serviceId: Int?,
    onSuccess: (userId: Int?, enterpriseId: Int) -> Unit,
    onBackClick: (userId: Int?, enterpriseId: Int) -> Unit
) {
    // Проверка обязательных параметров
    if (userId == null || enterpriseId == null || serviceId == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Ошибка: отсутствуют необходимые параметры",
                color = Color.White
            )
        }
        return
    }

    val viewModel: EmployeeSelectionViewModel = viewModel()
    val employeeViewModel: EmployeeViewModel = viewModel()
    val customFontFamily = FontFamily(
        Font(R.font.roboto_regular),
        Font(R.font.roboto_bold, FontWeight.Bold)
    )

    // Состояние выбранных сотрудников
    var selectedEmployees by rememberSaveable { mutableStateOf<Set<Int>>(emptySet()) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Загрузка данных
    LaunchedEffect(enterpriseId, serviceId) {
        try {
            isLoading = true
            employeeViewModel.loadCurrentEmployees(serviceId)
            viewModel.loadEmployee(enterpriseId)
        } catch (e: Exception) {
            errorMessage = "Ошибка загрузки данных: ${e.localizedMessage}"
            Log.e("LOAD_DATA", "Error loading data", e)
        } finally {
            isLoading = false
        }
    }

    // Обновление выбранных сотрудников
    LaunchedEffect(employeeViewModel.currentEmployees) {
        selectedEmployees = employeeViewModel.currentEmployees
    }

    val employees by viewModel.employee.collectAsState()

    // Диалог ошибки
    errorMessage?.let { message ->
        AlertDialog(
            onDismissRequest = { errorMessage = null },
            title = { Text("Ошибка") },
            text = { Text(message) },
            confirmButton = {
                Button(
                    onClick = { errorMessage = null },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black
                    )
                ) {
                    Text("OK", color = Color.White)
                }
            }
        )
    }

    // Основной интерфейс
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Шапка экрана
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 56.dp, bottom = 32.dp)
            ) {
                IconButton(
                    onClick = { onBackClick(userId, enterpriseId) },
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
                    text = "Редактировать привязанных сотрудников",
                    style = TextStyle(
                        fontSize = 22.sp,
                        fontFamily = customFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        letterSpacing = 0.5.sp
                    ),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth()
                        .padding(horizontal = 54.dp),
                    textAlign = TextAlign.Center
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
                    isLoading -> {
                        Box(modifier = Modifier.fillMaxSize()) {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                        }
                    }
                    employees.isEmpty() -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 24.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Нет доступных сотрудников",
                                color = Color.Gray,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                    else -> {
                        Column(modifier = Modifier.fillMaxSize()) {
                            LazyColumn(
                                modifier = Modifier.weight(1f),
                                contentPadding = PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(employees, key = { it.id }) { employee ->
                                    val isSelected = selectedEmployees.contains(employee.id)

                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                selectedEmployees = if (isSelected) {
                                                    selectedEmployees - employee.id
                                                } else {
                                                    selectedEmployees + employee.id
                                                }
                                                Log.d("SELECTION", "Selected IDs: $selectedEmployees")
                                            },
                                        shape = RoundedCornerShape(12.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = if (isSelected) Color.LightGray else Color.Black,
                                            contentColor = if (isSelected) Color.Black else Color.White
                                        ),
                                        elevation = CardDefaults.cardElevation(
                                            defaultElevation = if (isSelected) 8.dp else 2.dp
                                        )
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(16.dp)
                                        ) {
                                            Text(
                                                text = employee.employee_fio,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 16.sp
                                            )
                                            Text(
                                                text = employee.position,
                                                fontSize = 14.sp
                                            )
                                        }
                                    }
                                }
                            }

                            Button(
                                onClick = {
                                    try {
                                        employeeViewModel.updateServiceEmployees(
                                            serviceId = serviceId,
                                            employeeIds = selectedEmployees.toList(),
                                            onSuccess = { onSuccess(userId, enterpriseId) }
                                        )
                                    } catch (e: Exception) {
                                        errorMessage = "Ошибка при сохранении: ${e.localizedMessage}"
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp)
                                    .height(52.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (selectedEmployees.isNotEmpty()) Color.Black else Color.Gray,
                                    contentColor = Color.White
                                ),
                                enabled = selectedEmployees.isNotEmpty()
                            ) {
                                Text(
                                    text = if (selectedEmployees.isNotEmpty()) {
                                        "Сохранить (${selectedEmployees.size})"
                                    } else {
                                        "Сохранить"
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    fontFamily = customFontFamily,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}