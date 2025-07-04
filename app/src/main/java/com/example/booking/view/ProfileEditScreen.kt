package com.example.booking.view

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.booking.R
import com.example.booking.viewmodel.RegistrationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEditScreen( userId: Int?, enterpriseId: Int?, onSaveClick: (userId: Int?, enterpriseId: Int?) -> Unit, onBackClick: (userId: Int?, enterpriseId: Int?) -> Unit) {
    val customFontFamily = FontFamily(
        Font(R.font.roboto_regular),
        Font(R.font.roboto_bold, FontWeight.Bold)
    )

    var fullName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val viewModel: RegistrationViewModel = viewModel()

    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(userId) {
        if (userId != null) {
            viewModel.loadUserProfile(
                userId = userId,
                onSuccess = { user ->
                    fullName = user.fullName ?: ""
                    phoneNumber = user.phoneNumber ?: ""
                    password = user.password ?: ""
                }
            )
        }
    }

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest =  { showErrorDialog = false },
            title = null,
            text = {
                Text(
                    text = errorMessage,
                    style = TextStyle(
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick =  { showErrorDialog = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black
                    )
                ) {
                    Text(
                        text = "Ок",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            },
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .padding(horizontal = 24.dp),
            containerColor = Color.White,
            tonalElevation = 0.dp
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
                    onClick = {onBackClick(userId, enterpriseId)},
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 16.dp, end = 32.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_close),
                        contentDescription = "Закрыть",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Text(
                    text = "Информация о пользователе",
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
                        .padding(horizontal = 32.dp),
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
                Column(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .padding(top = 32.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    val disabledFieldColor = Color(0xFFF5F5F5)

                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = { Text("ФИО", fontFamily = customFontFamily) },
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
                        textStyle = TextStyle(fontFamily = customFontFamily, fontSize = 16.sp)
                    )

                    OutlinedTextField(
                        value = phoneNumber,
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
                        value = "********",
                        onValueChange = {},
                        label = {
                            Text(
                                "Пароль",
                                fontFamily = customFontFamily,
                                color = Color.Gray
                            )
                        },
                        visualTransformation = PasswordVisualTransformation(),
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
                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = {
                            viewModel.updateUserProfile(
                                userId, fullName, phoneNumber, password,
                                onSuccess = {
                                    Log.d("NAVIGATION", "Navigating to profile with userId: $userId, enterpriseId: $enterpriseId")
                                    onSaveClick(userId, enterpriseId)
                                }
                            )
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

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}