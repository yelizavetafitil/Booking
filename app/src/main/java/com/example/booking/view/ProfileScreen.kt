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
fun ProfileScreen(
    userId: Int?,
    enterpriseId: Int?,
    onProfileClick: (userId: Int?, enterpriseId: Int?) -> Unit,
    onEnterprisesClick: (userId: Int?, enterpriseId: Int?) -> Unit,
    onLogoutClick: () -> Unit,
    onManagementClick: (userId: Int?, enterpriseId: Int?) -> Unit,
    onCalendarClick: (userId: Int?, enterpriseId: Int?) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()
    var showLogoutDialog by remember { mutableStateOf(false) }

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
                    text = "Профиль",
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
                            Log.d("ENTERPRISE_SELECT",
                                "Enterpise ID: ${enterpriseId}, User ID: $userId")

                            onProfileClick(userId, enterpriseId)
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
                                "Профиль",
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
                        onClick = {
                            onEnterprisesClick(userId, enterpriseId)
                        },
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
                                "Предприятия",
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
                        onClick = { showLogoutDialog = true },
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
                                "Выйти",
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

                    if (showLogoutDialog) {
                        AlertDialog(
                            onDismissRequest = { showLogoutDialog = false },
                            title = {
                                Text(
                                    "Подтверждение выхода",
                                    style = TextStyle(
                                        fontFamily = customFontFamily,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            },
                            text = {
                                Text(
                                    "Вы уверены, что хотите выйти из аккаунта?",
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        fontFamily = customFontFamily
                                    )
                                )
                            },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        showLogoutDialog = false
                                        onLogoutClick()
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
                                    onClick = { showLogoutDialog = false }
                                ) {
                                    Text("Нет")
                                }
                            }
                        )
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
                        IconButton(onClick = {onManagementClick(userId, enterpriseId)} ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_management),
                                contentDescription = "Управление",
                                tint = Color.Unspecified,
                                modifier = Modifier.size(44.dp)
                            )
                        }
                        IconButton(onClick = {} ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_profile_select),
                                contentDescription = "Профиль",
                                tint = Color.Unspecified,
                                modifier = Modifier.size(56.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
