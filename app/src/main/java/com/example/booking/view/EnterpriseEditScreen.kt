package com.example.booking.view

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.booking.R
import com.example.booking.viewmodel.EnterpriseRegistrationViewModel
import kotlin.toString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnterpriseEditScreen(
    userId: Int?,
    enterpriseId: Int?,
    onSaveClick: (userId: Int?, enterpriseId: Int?) -> Unit,
    onDeleteClick: (userId: Int?) -> Unit,
    onBackClick: (userId: Int?, enterpriseId: Int?) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()
    var showDeleteDialog by remember { mutableStateOf(false) }

    val customFontFamily = FontFamily(
        Font(R.font.roboto_regular),
        Font(R.font.roboto_bold, FontWeight.Bold)
    )

    var enterpriseName by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var enterprisePhoneNumber by remember { mutableStateOf("") }
    val viewModel: EnterpriseRegistrationViewModel = viewModel()

    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    BackHandler {
        keyboardController?.hide()
        userId?.let(viewModel::backEnterprise)
        onBackClick(userId, enterpriseId)
    }

    LaunchedEffect(enterpriseId) {
        if (enterpriseId != null) {
            viewModel.loadEnterprise(
                enterpriseId = enterpriseId,
                onSuccess = { enter ->
                    enterpriseName = enter.enterpriseName ?: ""
                    city = enter.city ?: ""
                    address = enter.address ?: ""
                    enterprisePhoneNumber = enter.enterprisePhoneNumber ?: ""
                }
            )
        }
    }


    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = {
                Text(
                    "Ошибка",
                    style = TextStyle(fontFamily = customFontFamily)
                )
            },
            text = {
                Text(
                    text = errorMessage,
                    style = TextStyle(
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        fontFamily = customFontFamily
                    )
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showErrorDialog = false
                        keyboardController?.hide()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("OK", color = Color.White)
                }
            },
            modifier = Modifier.padding(horizontal = 24.dp)
        )
    }

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
                    .padding(top = 56.dp, bottom = 32.dp)
            ) {
                IconButton(
                    onClick = {
                        onBackClick(userId, enterpriseId)
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
                    text = "Информация о предприятии",
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
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = enterpriseName,
                        onValueChange = { enterpriseName = it },
                        label = { Text("Имя предприятия", fontFamily = customFontFamily) },
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
                        textStyle = TextStyle(fontFamily = customFontFamily, fontSize = 16.sp),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        )
                    )

                    OutlinedTextField(
                        value = city,
                        onValueChange = { city = it },
                        label = { Text("Город", fontFamily = customFontFamily) },
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
                        textStyle = TextStyle(fontFamily = customFontFamily, fontSize = 16.sp),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        )
                    )

                    OutlinedTextField(
                        value = address,
                        onValueChange = { address = it },
                        label = { Text("Адрес", fontFamily = customFontFamily) },
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
                        textStyle = TextStyle(fontFamily = customFontFamily, fontSize = 16.sp),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        )
                    )

                    val disabledFieldColor = Color(0xFFF5F5F5)

                    OutlinedTextField(
                        value = enterprisePhoneNumber,
                        onValueChange = {},
                        label = {
                            Text(
                                "Номер телефона предприятия",
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

                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = {
                            keyboardController?.hide()

                            if (userId == null) {
                                errorMessage = "Необходимо указать идентификатор пользователя"
                                showErrorDialog = true
                                return@Button
                            }

                            viewModel.updateEnterprise(enterpriseId, enterpriseName, city
                            , address, enterprisePhoneNumber,
                                onSuccess = {
                                    onSaveClick(userId, enterpriseId)
                                })
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

                    Button(
                        onClick = {
                            keyboardController?.hide()
                            showDeleteDialog=true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black,
                            contentColor = Color.Red
                        )
                    ) {
                        Text(
                            "Удалить",
                            fontFamily = customFontFamily,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    if (showDeleteDialog) {
                        AlertDialog(
                            onDismissRequest = { showDeleteDialog = false },
                            title = {
                                Text(
                                    "Подтверждение удаления",
                                    style = TextStyle(
                                        fontFamily = customFontFamily,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            },
                            text = {
                                Text(
                                    "Вы уверены, что хотите удалить предприятие?",
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        fontFamily = customFontFamily
                                    )
                                )
                            },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        showDeleteDialog = false
                                        if(enterpriseId==null){
                                            return@Button
                                        }
                                        viewModel.deleteEnterprise(enterpriseId, onSuccess = {
                                            onDeleteClick(userId)
                                        })
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
                                    onClick = { showDeleteDialog = false }
                                ) {
                                    Text("Нет", color = Color.Black)
                                }
                            },
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .padding(horizontal = 24.dp),
                            containerColor = Color.White,
                            tonalElevation = 0.dp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}