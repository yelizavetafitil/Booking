package com.example.booking.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.booking.R
import com.example.booking.viewmodel.EmployeeViewModel

@Composable
fun ChartEmployeeSetUpScreen(
    userId: Int?,
    enterpriseId: Int?,
    employeeId: Int?,
    onBackClick: (userId: Int?, enterpriseId: Int) -> Unit,
    onNextClick: (userId: Int?, enterpriseId: Int, employeeId: Int, level: String) -> Unit,
    onNextWeekClick: (userId: Int?, enterpriseId: Int, employeeId: Int, level: String) -> Unit,
    onNextChartClick: (userId: Int?, enterpriseId: Int, employeeId: Int, level: String) -> Unit,
    onNextChartExeptClick: (userId: Int?, enterpriseId: Int, employeeId: Int, level: String) -> Unit
) {
    val chartLevels = listOf(
        ChartLevel("Будни"),
        ChartLevel("Все дни"),
        ChartLevel("Четные"),
        ChartLevel("Нечетные"),
        ChartLevel("2 на 2"),
        ChartLevel("По дням недели"),
        ChartLevel("Настроить свою схему"),
        ChartLevel("День-исключение")
    )

    val customFontFamily = FontFamily(
        Font(R.font.roboto_regular),
        Font(R.font.roboto_bold, FontWeight.Bold)
    )

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
                    text = "Цикличный график",
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
                        .padding(horizontal = 16.dp)
                        .padding(top = 16.dp, bottom = 24.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    chartLevels.forEach { level ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (userId != null && enterpriseId != null && employeeId != null
                                        && level.title != "По дням недели"
                                        && level.title != "Настроить свою схему"
                                        && level.title != "День-исключение") {
                                        onNextClick(userId, enterpriseId, employeeId, level.title)
                                    } else if (userId != null && enterpriseId != null && employeeId != null
                                        && level.title == "По дням недели"){
                                        onNextWeekClick(userId, enterpriseId, employeeId, level.title)
                                    }else if (userId != null && enterpriseId != null && employeeId != null
                                        && level.title == "Настроить свою схему"){
                                        onNextChartClick(userId, enterpriseId, employeeId, level.title)
                                    }else if (userId != null && enterpriseId != null && employeeId != null
                                        && level.title == "День-исключение"){
                                        onNextChartExeptClick(userId, enterpriseId, employeeId, level.title)
                                    }
                                },
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
                                    text = level.title,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

data class ChartLevel(val title: String)