package com.example.booking.view

import android.service.autofill.DateTransformation
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.booking.R
import com.example.booking.models.*
import com.example.booking.viewmodel.WorkingHoursViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChartChoiceHoursScreen(
    userId: Int?,
    enterpriseId: Int?,
    employeeId: Int?,
    level: String?,
    dayWork: String?,
    dayRest: String?,
    subType:  String?,
    onBackClick: (userId: Int?, enterpriseId: Int?, employeeId: Int?, level: String?, dayWork: String?,  dayRest: String?) -> Unit,
    onSaveClick: (userId: Int?, enterpriseId: Int?) -> Unit,
) {

    val viewModel: WorkingHoursViewModel = viewModel()

    val customFontFamily = FontFamily(
        Font(R.font.roboto_regular),
        Font(R.font.roboto_bold, FontWeight.Bold)
    )

    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var breakStart by remember { mutableStateOf("") }
    var breakEnd by remember { mutableStateOf("") }

    LaunchedEffect(employeeId, level) {
        if (employeeId != null && level != null && dayWork != null&& dayRest != null&& subType != null) {
            viewModel.loadWorkingChoiceHours(employeeId, level, dayWork, dayRest, subType)
        }
    }

    viewModel.workingChoiceHours?.let { hours ->
        if (startTime.isEmpty() && endTime.isEmpty()) {
            startTime = hours.workTime.start
            endTime = hours.workTime.end
            startDate = hours.period.start.convertDateFormat()
            endDate = hours.period.end.convertDateFormat()
            hours.breaks.firstOrNull()?.let { breakTime ->
                breakStart = breakTime.start
                breakEnd = breakTime.end
            }
        }
    }

    if (viewModel.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    viewModel.error?.let { errorMessage ->
        AlertDialog(
            onDismissRequest = { viewModel.clearError() },
            title = { Text("Ошибка") },
            text = { Text(errorMessage) },
            confirmButton = {
                Button(onClick = { viewModel.clearError() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black
                    )) {
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
                        if (userId != null && enterpriseId != null && employeeId != null && level != null&& dayWork != null&& dayRest != null) {
                            onBackClick(userId, enterpriseId, employeeId, level, dayWork, dayRest)
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
                    text = "Рабочее время",
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
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .clickable {},
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Black,
                            contentColor = Color.White
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Введите рабочее время",
                                fontFamily = customFontFamily,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TimeInputField(
                            value = startTime,
                            onValueChange = { startTime = it },
                            modifier = Modifier.weight(1f),
                            fontFamily = customFontFamily,
                            placeholder = "Начало"
                        )

                        Text(
                            text = "—",
                            modifier = Modifier.padding(horizontal = 8.dp),
                            fontFamily = customFontFamily
                        )

                        TimeInputField(
                            value = endTime,
                            onValueChange = { endTime = it },
                            modifier = Modifier.weight(1f),
                            fontFamily = customFontFamily,
                            placeholder = "Конец"
                        )
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .clickable {},
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Black,
                            contentColor = Color.White
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Введите рабочий период",
                                fontFamily = customFontFamily,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        DateInputField(
                            value = startDate,
                            onValueChange = { startDate = it },
                            modifier = Modifier.weight(1f),
                            fontFamily = customFontFamily,
                            placeholder = "ДД.ММ.ГГ"
                        )

                        Text(
                            text = "—",
                            modifier = Modifier.padding(horizontal = 8.dp),
                            fontFamily = customFontFamily
                        )

                        DateInputField(
                            value = endDate,
                            onValueChange = { endDate = it },
                            modifier = Modifier.weight(1f),
                            fontFamily = customFontFamily,
                            placeholder = "ДД.ММ.ГГ"
                        )
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .clickable {},
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Black,
                            contentColor = Color.White
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Перерыв",
                                fontFamily = customFontFamily,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TimeInputField(
                            value = breakStart,
                            onValueChange = { breakStart = it },
                            modifier = Modifier.weight(1f),
                            fontFamily = customFontFamily,
                            placeholder = "Начало"
                        )

                        Text(
                            text = "—",
                            modifier = Modifier.padding(horizontal = 8.dp),
                            fontFamily = customFontFamily
                        )

                        TimeInputField(
                            value = breakEnd,
                            onValueChange = { breakEnd = it },
                            modifier = Modifier.weight(1f),
                            fontFamily = customFontFamily,
                            placeholder = "Конец"
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = {
                            if (userId != null && enterpriseId != null && employeeId != null && level != null && dayWork != null&& dayRest != null&& subType != null) {
                                val breaks = if (breakStart.isNotEmpty() && breakEnd.isNotEmpty()) {
                                    listOf(BreakTime(breakStart, breakEnd))
                                } else {
                                    emptyList()
                                }

                                val workingHoursData = WorkingHoursData(
                                    scheduleType = level,
                                    workTime = WorkTime(startTime, endTime),
                                    period = WorkPeriod(startDate, endDate),
                                    breaks = breaks
                                )

                                viewModel.saveWorkingChoiceHours(
                                    employeeId = employeeId,
                                    scheduleType = level,
                                    dayWork = dayWork,
                                    dayRest = dayRest,
                                    scheduleSubType = subType,
                                    workTime = workingHoursData.workTime,
                                    workPeriod = workingHoursData.period,
                                    breaks = workingHoursData.breaks,
                                    onSuccess = {
                                        onSaveClick(userId, enterpriseId)
                                    }
                                )
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

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}
