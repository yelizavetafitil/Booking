package com.example.booking.view

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.booking.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagementScreen(
    userId: Int?,
    enterpriseId: Int?,
    onAnalyticsClick: (userId: Int?, enterpriseId: Int?) -> Unit,
    onEmployeeClick: (userId: Int?, enterpriseId: Int?) -> Unit,
    onServiceClick: (userId: Int?, enterpriseId: Int?) -> Unit,
    onProfileClick: (userId: Int?, enterpriseId: Int?) -> Unit,
    onCalendarClick: (userId: Int?, enterpriseId: Int?) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()

    val customFontFamily = FontFamily(
        Font(R.font.roboto_regular),
        Font(R.font.roboto_bold, FontWeight.Bold)
    )

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                keyboardController?.hide()
                focusManager.clearFocus()
            },
        color = Color.Black
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 64.dp, bottom = 32.dp)
            ) {
                Text(
                    text = "Управление",
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
                    verticalArrangement = Arrangement.spacedBy(0.dp)
                ) {

                    Button(
                        onClick = {
                            onAnalyticsClick(userId, enterpriseId)
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
                                "Аналитика",
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

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
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
                                "Зарплата",
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

                    HorizontalDivider(color = Color.Gray, thickness = 1.dp)

                    Button(
                        onClick = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(0.dp),
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
                                "Продажи",
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

                    HorizontalDivider(color = Color.Gray, thickness = 1.dp)

                    Button(
                        onClick = {  },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
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
                                "Финансы",
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

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            onEmployeeClick(userId, enterpriseId)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
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
                                "Сотрудники",
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

                    HorizontalDivider(color = Color.Gray, thickness = 1.dp)

                    Button(
                        onClick = { onServiceClick(userId, enterpriseId) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
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
                                "Услуги",
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

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Surface(
                    modifier = Modifier,
                    color = Color.Black,
                ){
                    Row(
                        modifier = Modifier
                            .padding(top = 8.dp, bottom = 20.dp, end = 16.dp, start = 16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = {onCalendarClick(userId, enterpriseId)}
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_calendar),
                                contentDescription = "Календарь",
                                tint = Color.Unspecified,
                                modifier = Modifier.size(44.dp)
                            )
                        }
                        IconButton(onClick = {} ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_management_select),
                                contentDescription = "Управление",
                                tint = Color.Unspecified,
                                modifier = Modifier.size(56.dp)
                            )
                        }
                        IconButton(onClick = {onProfileClick(userId, enterpriseId)} ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_profile),
                                contentDescription = "Профиль",
                                tint = Color.Unspecified,
                                modifier = Modifier.size(44.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
