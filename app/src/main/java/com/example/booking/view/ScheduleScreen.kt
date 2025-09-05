package com.example.booking.view

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.booking.R
import com.example.booking.viewmodel.ScheduleViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScheduleScreen(
    userId: Int?,
    enterpriseId: Int?,
    onBackClick: (userId: Int?, enterpriseId: Int?) -> Unit,
    onConfigureClick: (userId: Int?, enterpriseId: Int?) -> Unit,
) {
    val viewModel: ScheduleViewModel = viewModel()
    val customFontFamily = FontFamily(
        Font(R.font.roboto_regular),
        Font(R.font.roboto_bold, FontWeight.Bold)
    )

    var selectedDay by remember { mutableStateOf(LocalDate.now()) }
    var showDatePicker by remember { mutableStateOf(false) }
    val visibleDates = remember { mutableStateListOf<LocalDate>() }
    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(selectedDay) {
        viewModel.loadSchedules(selectedDay)
    }

    val currentVisibleMonth by remember {
        derivedStateOf {
            val layoutInfo = scrollState.layoutInfo
            if (layoutInfo.visibleItemsInfo.isEmpty()) {
                YearMonth.from(selectedDay)
            } else {
                val centerIndex = scrollState.firstVisibleItemIndex + layoutInfo.visibleItemsInfo.size / 2
                val adjustedIndex = centerIndex.coerceIn(0, visibleDates.lastIndex)
                YearMonth.from(visibleDates[adjustedIndex])
            }
        }
    }

    LaunchedEffect(Unit) {
        visibleDates.addAll(generateDatesAround(selectedDay, 30))
        coroutineScope.launch {
            delay(300)
            val todayIndex = visibleDates.indexOfFirst { it.isEqual(selectedDay) }
            if (todayIndex != -1) {
                scrollState.animateScrollToItem(todayIndex)
            }
        }
    }


    val currentMonthName = remember(currentVisibleMonth) {
        currentVisibleMonth.month.getDisplayName(TextStyle.FULL_STANDALONE, Locale("ru"))
            .replaceFirstChar { it.titlecase() } + " " + currentVisibleMonth.year
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, bottom = 16.dp)
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
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Text(
                    text = "График",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 22.sp,
                        fontFamily = customFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                    ),
                    modifier = Modifier.align(Alignment.Center)
                )

                IconButton(
                    onClick = { showDatePicker = true },
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 16.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_calen),
                        contentDescription = "Календарь",
                        tint = Color.Black,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }

            Text(
                text = currentMonthName,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                style = MaterialTheme.typography.titleMedium,
                fontFamily = customFontFamily
            )

            InfiniteDaysRow(
                selectedDay = selectedDay,
                visibleDates = visibleDates,
                scrollState = scrollState,
                coroutineScope = coroutineScope,
                onDaySelected = { date ->
                    selectedDay = date
                },
                fontFamily = customFontFamily
            )

            when {
                viewModel.isLoading -> {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                viewModel.error != null -> {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = viewModel.error!!,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp)
                    ) {
                        viewModel.schedules.forEach { employeeSchedule ->
                            employeeSchedule.timeSlots.forEach { slot ->
                                item {
                                    TimeSlotItem(
                                        employeeName = employeeSchedule.fullName,
                                        startTime = slot.startTime,
                                        endTime = slot.endTime,
                                        fontFamily = customFontFamily,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Button(
                onClick = {
                    if (userId != null && enterpriseId != null) {
                        onConfigureClick(userId, enterpriseId)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                )
            ) {
                Text(
                    "Настроить",
                    fontFamily = customFontFamily,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        if (showDatePicker) {
            DatePickerDialog(
                onDismiss = { showDatePicker = false },
                onDateSelected = { date ->
                    selectedDay = date
                    showDatePicker = false

                    if (visibleDates.none { it.isEqual(date) }) {
                        visibleDates.clear()
                        visibleDates.addAll(generateDatesAround(date, 30))
                    }

                    coroutineScope.launch {
                        val index = visibleDates.indexOfFirst { it.isEqual(date) }
                        if (index != -1) {
                            scrollState.animateScrollToItem(index)
                        }
                    }
                }
            )
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun InfiniteDaysRow(
    selectedDay: LocalDate,
    visibleDates: MutableList<LocalDate>,
    scrollState: LazyListState,
    coroutineScope: CoroutineScope,
    onDaySelected: (LocalDate) -> Unit,
    fontFamily: FontFamily
) {
    LazyRow(
        state = scrollState,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        items(visibleDates.size, key = { visibleDates[it].toString() }) { index ->
            val date = visibleDates[index]
            DayOfWeekButton(
                date = date,
                isSelected = date.isEqual(selectedDay),
                onClick = { onDaySelected(date) },
                fontFamily = fontFamily,
                modifier = Modifier.padding(horizontal = 4.dp)
            )

            LaunchedEffect(index) {
                if (index == visibleDates.size - 5) {
                    val lastDate = visibleDates.last()
                    val newDates = generateDatesAfter(lastDate, 30)
                    visibleDates.addAll(newDates)
                }

                if (index == 5) {
                    val firstDate = visibleDates.first()
                    val newDates = generateDatesBefore(firstDate, 30)
                    val prevSize = visibleDates.size
                    visibleDates.addAll(0, newDates)
                    coroutineScope.launch {
                        scrollState.scrollToItem(index + newDates.size)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DatePickerDialog(
    onDismiss: () -> Unit,
    onDateSelected: (LocalDate) -> Unit
) {

    val defaultLocale = Locale("ru", "RU")
    val currentLocale = remember { defaultLocale }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = System.currentTimeMillis(),
            yearRange = IntRange(LocalDate.now().year - 1, LocalDate.now().year + 1),
            initialDisplayMode = DisplayMode.Picker
        )

        val confirmEnabled = remember { derivedStateOf { datePickerState.selectedDateMillis != null } }

        CompositionLocalProvider(
            LocalConfiguration provides Configuration(LocalConfiguration.current).apply {
                setLocale(currentLocale)
            }
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    DatePicker(
                        state = datePickerState,
                        title = {
                            Text(
                                text = "Выберите дату",
                                modifier = Modifier.padding(12.dp),
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.Black
                            )
                        },
                        showModeToggle = false,
                        modifier = Modifier.padding(horizontal = 8.dp),
                        colors = DatePickerDefaults.colors(
                            containerColor = Color.White,
                            titleContentColor = Color.Black,
                            headlineContentColor = Color.Black,
                            weekdayContentColor = Color.Black,
                            subheadContentColor = Color.Black,
                            yearContentColor = Color.Black,
                            currentYearContentColor = Color.Blue,
                            selectedYearContentColor = Color.White,
                            disabledYearContentColor = Color.Gray,
                            selectedYearContainerColor = Color.Black,
                            dayContentColor = Color.Black,
                            disabledDayContentColor = Color.Gray,
                            selectedDayContentColor = Color.White,
                            selectedDayContainerColor = Color.Black,
                            todayContentColor = Color.Blue,
                            todayDateBorderColor = Color.Blue,
                            navigationContentColor = Color.Black
                        )
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = onDismiss) {
                            Text("Отмена", color = Color.Black)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        TextButton(
                            onClick = {
                                datePickerState.selectedDateMillis?.let {
                                    onDateSelected(
                                        Instant.ofEpochMilli(it)
                                            .atZone(ZoneId.systemDefault())
                                            .toLocalDate()
                                    )
                                }
                            },
                            enabled = confirmEnabled.value
                        ) {
                            Text("Выбрать", color = Color.Black)
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DayOfWeekButton(
    date: LocalDate,
    isSelected: Boolean,
    onClick: () -> Unit,
    fontFamily: FontFamily,
    modifier: Modifier = Modifier
) {
    val dayOfWeek = date.dayOfWeek
    val dayName = dayOfWeek.getDisplayName(TextStyle.SHORT_STANDALONE, Locale("ru")).take(2)
    val dayNumber = date.dayOfMonth.toString().padStart(2, '0')

    val backgroundColor = if (isSelected) Color.Black else Color(0xFFF5F5F5)
    val textColor = if (isSelected) Color.White else Color.Black

    Box(
        modifier = modifier
            .size(48.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(backgroundColor)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = dayName,
                color = textColor,
                fontFamily = fontFamily,
                fontSize = 12.sp
            )
            Text(
                text = dayNumber,
                color = textColor,
                fontFamily = fontFamily,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun TimeSlotItem(
    employeeName: String,
    startTime: String,
    endTime: String,
    fontFamily: FontFamily,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = employeeName,
                fontFamily = fontFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Text(
                text = "$startTime - $endTime",
                fontFamily = fontFamily,
                fontSize = 16.sp,
                color = Color.Black
            )
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
fun generateDatesAround(centerDate: LocalDate, daysAround: Int): List<LocalDate> {
    return (-daysAround..daysAround).map { centerDate.plusDays(it.toLong()) }
}

@RequiresApi(Build.VERSION_CODES.O)
fun generateDatesAfter(lastDate: LocalDate, count: Int): List<LocalDate> {
    return (1..count).map { lastDate.plusDays(it.toLong()) }
}

@RequiresApi(Build.VERSION_CODES.O)
fun generateDatesBefore(firstDate: LocalDate, count: Int): List<LocalDate> {
    return (1..count).map { i -> firstDate.minusDays(i.toLong()) }.reversed()
}


data class EmployeeTimeSlot(
    val employeeName: String,
    val startTime: String,
    val endTime: String
)