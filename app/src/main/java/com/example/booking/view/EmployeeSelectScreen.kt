package com.example.booking.view

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.booking.viewmodel.EmployeeSelectionViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeSelectScreen(
    userId: Int?,
    enterpriseId: Int?,
    onEditEmployee: (userId: Int?, enterpriseId: Int, employeeId: Int) -> Unit,
    onAddEmployee: (userId: Int?, enterpriseId: Int) -> Unit,
    onBackClick: (userId: Int?, enterpriseId: Int) -> Unit
) {
    val customFontFamily = FontFamily(
        Font(R.font.roboto_regular),
        Font(R.font.roboto_bold, FontWeight.Bold)
    )

    val viewModel: EmployeeSelectionViewModel = viewModel()

    LaunchedEffect(enterpriseId) {
        try {
            enterpriseId?.let {
                viewModel.loadEmployee(it)
            } ?: run {
                Log.e("ENTERPRISE_LOAD", "User ID is null")
            }
        } catch (e: Exception) {
            Log.e("ENTERPRISE_LOAD", "Error loading enterprises", e)
        }
    }

    val employees by viewModel.employee.collectAsState()
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

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
                        try {
                            if (userId != null && enterpriseId != null) {
                                onBackClick(userId, enterpriseId)
                            } else {
                                errorMessage = "Идентификатор пользователя не может быть пустым"
                            }
                        } catch (e: Exception) {
                            errorMessage = "Ошибка навигации: ${e.localizedMessage}"
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
                    text = "Управление сотрудниками",
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
                        .padding(horizontal = 48.dp),
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
                Column(modifier = Modifier.fillMaxSize()) {
                    if (isLoading) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    } else {
                        if (employees.isEmpty()) {
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
                                Button(
                                    onClick = {
                                        if (userId == null || enterpriseId == null) {
                                            errorMessage =
                                                "Необходимо указать идентификатор пользователя"
                                            return@Button
                                        }
                                        onAddEmployee(userId, enterpriseId)
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 24.dp, vertical = 24.dp)
                                        .height(52.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.Black,
                                        contentColor = Color.White
                                    ),
                                    elevation = ButtonDefaults.buttonElevation(
                                        defaultElevation = 0.dp,
                                        pressedElevation = 0.dp
                                    )
                                ) {
                                    Text(
                                        "Добавить",
                                        fontFamily = customFontFamily,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                                    .padding(horizontal = 16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)
                            ) {
                                items(employees) { employee ->

                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor =Color.Black,
                                            contentColor =Color.White
                                        )
                                    ) {
                                        EmployeeItem(
                                            name = employee.employee_fio,
                                            role = employee.position,
                                            onClick = {
                                                try {
                                                    if (userId == null || enterpriseId==null) {
                                                        errorMessage = "Идентификатор пользователя не может быть пустым"
                                                    } else {
                                                        onEditEmployee(userId, enterpriseId, employee.id)
                                                    }
                                                } catch (e: Exception) {
                                                    errorMessage = "Ошибка выбора: ${e.localizedMessage}"
                                                }
                                            },
                                            textColor = Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Button(
                        onClick = {
                            try {
                                if (userId == null || enterpriseId == null) {
                                    errorMessage = "Необходимо указать идентификатор пользователя"
                                } else {
                                    onAddEmployee(userId, enterpriseId)
                                }
                            } catch (e: Exception) {
                                errorMessage = "Ошибка редактирования: ${e.localizedMessage}"
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 24.dp)
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black,
                            contentColor = Color.White
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 0.dp,
                            pressedElevation = 0.dp
                        )
                    ) {
                        Text(
                            "Добавить",
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

@Composable
fun EmployeeItem(
    name: String,
    role: String,
    onClick: () -> Unit,
    textColor: Color = Color.White
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Text(
            text = name,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = role,
            style = TextStyle(
                fontSize = 14.sp,
                color = textColor.copy(alpha = 0.8f)
            )
        )
    }
}