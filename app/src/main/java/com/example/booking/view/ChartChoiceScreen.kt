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
fun ChartChoiceScreen(
    userId: Int?,
    enterpriseId: Int?,
    employeeId: Int?,
    level: String?,
    onBackClick: (userId: Int?, enterpriseId: Int?, employeeId: Int?) -> Unit,
    onNextClick: (userId: Int?, enterpriseId: Int?, employeeId: Int?,  level: String?, dayWork: String?, dayRest: String?) -> Unit,
) {

    val viewModel: WorkingHoursViewModel = viewModel()

    val customFontFamily = FontFamily(
        Font(R.font.roboto_regular),
        Font(R.font.roboto_bold, FontWeight.Bold)
    )

    var startDay by remember { mutableStateOf("") }
    var endDay by remember { mutableStateOf("") }

    viewModel.error?.let { errorMessage ->
        AlertDialog(
            onDismissRequest = { viewModel.clearError() },
            title = {
                Text(
                    text = "Ошибка",
                    color = Color.Black
                )
            },
            text = {
                Text(
                    text = errorMessage,
                    color = Color.Black
                )
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.clearError() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    )
                ) {
                    Text("OK")
                }
            },
            containerColor = Color.White,
            titleContentColor = Color.Black,
            textContentColor = Color.Black
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
                        if (userId != null && enterpriseId != null && employeeId != null) {
                            onBackClick(userId, enterpriseId, employeeId)
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
                    text = "Рабочие дни",
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
                                text = "Введите количсетво рабочих дней",
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
                        DayOfWeekInputField(
                            value = startDay,
                            onValueChange = { startDay = it },
                            modifier = Modifier.weight(1f),
                            fontFamily = customFontFamily,
                            placeholder = "1-7"
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
                                text = "Введите количсетво выходных дней",
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
                        DayOfWeekInputField(
                            value = endDay,
                            onValueChange = { endDay = it },
                            modifier = Modifier.weight(1f),
                            fontFamily = customFontFamily,
                            placeholder = "1-7"
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = {
                            if (userId != null && enterpriseId != null && employeeId != null && level != null) {
                                onNextClick(userId, enterpriseId, employeeId, level, startDay, endDay)
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
                            "Далее",
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

@Composable
fun DayOfWeekInputField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    fontFamily: FontFamily,
    placeholder: String = "1-7"
) {
    var textFieldValue by remember(value) {
        mutableStateOf(TextFieldValue(
            text = value,
            selection = TextRange(value.length)
        ))
    }

    BasicTextField(
        value = textFieldValue,
        onValueChange = { newValue ->
            val filteredText = newValue.text.filter { it.isDigit() }.take(1)

            if (filteredText.isEmpty() || (filteredText.toIntOrNull() in 1..7)) {
                textFieldValue = newValue.copy(
                    text = filteredText,
                    selection = TextRange(filteredText.length)
                )
                onValueChange(filteredText)
            }
        },
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
        textStyle = TextStyle(
            fontFamily = fontFamily,
            fontSize = 16.sp,
            color = Color.Black
        ),
        decorationBox = { innerTextField ->
            Box {
                if (textFieldValue.text.isEmpty()) {
                    Text(
                        text = placeholder,
                        fontFamily = fontFamily,
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                }
                innerTextField()
            }
        }
    )
}
