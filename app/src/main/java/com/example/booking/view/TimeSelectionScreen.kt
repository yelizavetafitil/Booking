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
import com.example.booking.viewmodel.TimeSlotViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TimeSelectionScreen(
    userId: Int?,
    enterpriseId: Int?,
    serviceId: Int?,
    employeeId: Int?,
    onNext: (userId: Int?, enterpriseId: Int, serviceId: Int, employeeId: Int, selectedTime: String, selectedDay: LocalDate) -> Unit,
    onBackClick: (userId: Int?, enterpriseId: Int, serviceId: Int) -> Unit
) {
    val viewModel: TimeSlotViewModel = viewModel()
    val customFontFamily = FontFamily(
        Font(R.font.roboto_regular),
        Font(R.font.roboto_bold, FontWeight.Bold)
    )

    var selectedDay by remember { mutableStateOf(LocalDate.now()) }
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedTime by remember { mutableStateOf("") }
    val visibleDates = remember { mutableStateListOf<LocalDate>() }
    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val onTimeSlotSelected: (String) -> Unit = { time ->
        selectedTime = time
        if (userId != null && enterpriseId != null && serviceId != null && employeeId != null) {
            onNext(userId, enterpriseId, serviceId, employeeId, selectedTime, selectedDay)
        }
    }

    if (employeeId != null && serviceId != null) {
        LaunchedEffect(selectedDay) {
            viewModel.loadTimeSlots(
                employeeId = employeeId,
                serviceId = serviceId,
                date = selectedDay
            )
        }
    }

    val currentVisibleMonth by remember {
        derivedStateOf {
            val layoutInfo = scrollState.layoutInfo
            if (layoutInfo.visibleItemsInfo.isEmpty()) YearMonth.from(selectedDay)
            else {
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
            if (todayIndex != -1) scrollState.animateScrollToItem(todayIndex)
        }
    }

    val currentMonthName = remember(currentVisibleMonth) {
        currentVisibleMonth.month.getDisplayName(TextStyle.FULL_STANDALONE, Locale("ru"))
            .replaceFirstChar { it.titlecase() } + " " + currentVisibleMonth.year
    }

    Column(modifier = Modifier.fillMaxSize()) {
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(top = 24.dp, bottom = 16.dp)
        ) {
            IconButton(
                onClick = {
                    if (userId != null && enterpriseId != null && serviceId != null) {
                        onBackClick(userId, enterpriseId, serviceId)
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
                text = "Выберите время",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 22.sp,
                    fontFamily = customFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
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
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }
        }

        
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
        ) {
            
            Text(
                text = currentMonthName,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Color.White,
                    fontFamily = customFontFamily
                )
            )

            
            InfiniteDaysRow(
                selectedDay = selectedDay,
                visibleDates = visibleDates,
                scrollState = scrollState,
                coroutineScope = coroutineScope,
                onDaySelected = { date ->
                    selectedDay = date
                },
                fontFamily = customFontFamily,
                backgroundColor = Color.Black
            )
        }

        
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
            color = Color.White,
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    when {
                        viewModel.isLoading -> LoadingIndicator()
                        viewModel.error != null -> ErrorView(viewModel.error!!)
                        else -> TimeSlotsSection(
                            timeSlots = viewModel.timeSlots,
                            fontFamily = customFontFamily,
                            onTimeSlotSelected = onTimeSlotSelected
                        )
                    }
                }
            }
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
                    if (index != -1) scrollState.animateScrollToItem(index)
                }
            }
        )
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
    fontFamily: FontFamily,
    backgroundColor: Color
) {
    LazyRow(
        state = scrollState,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(backgroundColor),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        items(visibleDates.size, key = { visibleDates[it].toString() }) { index ->
            val date = visibleDates[index]
            DayItem(
                date = date,
                isSelected = date.isEqual(selectedDay),
                onClick = {
                    onDaySelected(date)
                    coroutineScope.launch {
                        scrollState.animateScrollToItem(index)
                    }
                },
                fontFamily = fontFamily,
                backgroundColor = backgroundColor
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun DayItem(
    date: LocalDate,
    isSelected: Boolean,
    onClick: () -> Unit,
    fontFamily: FontFamily,
    backgroundColor: Color
) {
    Column(
        modifier = Modifier
            .width(48.dp)
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        
        Text(
            text = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale("ru")),
            color = if (isSelected) Color.White else Color.White.copy(alpha = 0.7f),
            fontSize = 12.sp,
            fontFamily = fontFamily
        )

        
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(
                    if (isSelected) Color.White else Color.Transparent
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = date.dayOfMonth.toString(),
                color = if (isSelected) Color.Black else Color.White,
                fontSize = 16.sp,
                fontFamily = fontFamily,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}


@Composable
private fun TimeSlotsSection(
    timeSlots: Map<String, List<String>>,
    fontFamily: FontFamily,
    onTimeSlotSelected: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        
        item {
            TimeSlotHeader("Утро", fontFamily)
        }
        items(timeSlots["Утро"]?.size ?: 0) { index ->
            val timeSlot = timeSlots["Утро"]?.get(index) ?: ""
            val isBusy = timeSlot.contains("(занято)")

            TimeSlotItem(
                time = timeSlot,
                isBusy = isBusy,
                fontFamily = fontFamily,
                modifier = Modifier.padding(vertical = 4.dp),
                onClick = {
                    if (!isBusy) {
                        onTimeSlotSelected(timeSlot.replace(" (занято)", ""))
                    }
                }
            )
        }

        
        item {
            TimeSlotHeader("День", fontFamily)
        }
        items(timeSlots["День"]?.size ?: 0) { index ->
            val timeSlot = timeSlots["День"]?.get(index) ?: ""
            val isBusy = timeSlot.contains("(занято)")

            TimeSlotItem(
                time = timeSlot,
                isBusy = isBusy,
                fontFamily = fontFamily,
                modifier = Modifier.padding(vertical = 4.dp),
                onClick = {
                    if (!isBusy) {
                        onTimeSlotSelected(timeSlot.replace(" (занято)", ""))
                    }
                }
            )
        }

        
        item {
            TimeSlotHeader("Вечер", fontFamily)
        }
        items(timeSlots["Вечер"]?.size ?: 0) { index ->
            val timeSlot = timeSlots["Вечер"]?.get(index) ?: ""
            val isBusy = timeSlot.contains("(занято)")

            TimeSlotItem(
                time = timeSlot,
                isBusy = isBusy,
                fontFamily = fontFamily,
                modifier = Modifier.padding(vertical = 4.dp),
                onClick = {
                    if (!isBusy) {
                        onTimeSlotSelected(timeSlot.replace(" (занято)", ""))
                    }
                }
            )
        }
    }
}

@Composable
private fun TimeSlotHeader(
    title: String,
    fontFamily: FontFamily
) {
    Text(
        text = title,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 8.dp),
        fontFamily = fontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        color = Color.Black
    )
}

@Composable
private fun TimeSlotItem(
    time: String,
    isBusy: Boolean,
    fontFamily: FontFamily,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable(
                enabled = !isBusy,
                onClick = onClick
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isBusy) Color.Gray else Color.Black,
            contentColor = if (isBusy) Color.White.copy(alpha = 0.5f) else Color.White
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = time,
                fontFamily = fontFamily,
                fontSize = 16.sp,
                color = if (isBusy) Color.White.copy(alpha = 0.5f) else Color.White
            )
        }
    }
}

@Composable
private fun LoadingIndicator() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorView(error: String) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = error,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(16.dp)
        )
    }
}
