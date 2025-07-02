package com.example.pr35

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.pr35.data.AppDatabase
import com.example.pr35.EditProfileViewModel
import com.example.pr35.ui.theme.PR35Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(navController: NavController, viewModel: EditProfileViewModel = viewModel(
    factory = EditProfileViewModel.Factory(AppDatabase.getDatabase(navController.context).userDao())
)) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Редактировать профиль", fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Назад")
                    }
                },
                actions = {
                    Button(
                        onClick = {
                            viewModel.saveProfile {
                                navController.popBackStack() // Вернуться на предыдущий экран после сохранения
                            }
                        },
                        enabled = uiState.isFormValid,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text(text = "Сохранить")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Изменить фото профиля",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 16.sp,
                modifier = Modifier.clickable { /* TODO: Обработка изменения фото */ }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Input Fields
            OutlinedTextField(
                value = uiState.firstName,
                onValueChange = { viewModel.updateFirstName(it) },
                label = { Text("Имя") },
                isError = uiState.firstNameError != null,
                supportingText = {
                    if (uiState.firstNameError != null) {
                        Text(uiState.firstNameError!!, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.lastName,
                onValueChange = { viewModel.updateLastName(it) },
                label = { Text("Фамилия") },
                isError = uiState.lastNameError != null,
                supportingText = {
                    if (uiState.lastNameError != null) {
                        Text(uiState.lastNameError!!, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.address,
                onValueChange = { viewModel.updateAddress(it) },
                label = { Text("Адрес") },
                isError = uiState.addressError != null,
                supportingText = {
                    if (uiState.addressError != null) {
                        Text(uiState.addressError!!, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.phoneNumber,
                onValueChange = { viewModel.updatePhoneNumber(it) },
                label = { Text("Телефон") },
                isError = uiState.phoneNumberError != null,
                supportingText = {
                    if (uiState.phoneNumberError != null) {
                        Text(uiState.phoneNumberError!!, color = MaterialTheme.colorScheme.error)
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Save button is in TopAppBar actions
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditProfileScreenPreview() {
    PR35Theme {
        EditProfileScreen(rememberNavController())
    }
}