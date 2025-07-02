package com.example.pr35

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.pr35.data.User
import com.example.pr35.data.UserDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

data class EditProfileUiState(
    val id: Int = 0, // ID пользователя, 0 если новый
    val firstName: String = "",
    val lastName: String = "",
    val address: String = "",
    val phoneNumber: String = "",
    val firstNameError: String? = null,
    val lastNameError: String? = null,
    val addressError: String? = null,
    val phoneNumberError: String? = null,
    val isFormValid: Boolean = false
)

class EditProfileViewModel(private val userDao: UserDao) : ViewModel() {

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    private var currentUser: User? = null // Для отслеживания текущего пользователя из БД

    init {
        // Загружаем существующие данные пользователя при инициализации ViewModel
        viewModelScope.launch {
            userDao.getUser().collect { user ->
                currentUser = user
                _uiState.value = _uiState.value.copy(
                    id = user?.id ?: 0,
                    firstName = user?.firstName ?: "",
                    lastName = user?.lastName ?: "",
                    address = user?.address ?: "",
                    phoneNumber = user?.phoneNumber ?: ""
                )
                validateForm() // Валидируем форму после загрузки данных
            }
        }

        // Комбинируем все ошибки валидации и обновляем isFormValid

    }

    // Helper для combine и collectIn
    private fun <T1, T2, T3, T4, T5, R> Flow<T1>.combine(
        flow2: Flow<T2>,
        flow3: Flow<T3>,
        flow4: Flow<T4>,
        flow5: Flow<T5>,
        transform: suspend (T1, T2, T3, T4, T5) -> R
    ): Flow<R> = kotlinx.coroutines.flow.combine(this, flow2, flow3, flow4, flow5, transform)

    private suspend fun <T> Flow<T>.collectIn(scope: kotlinx.coroutines.CoroutineScope, action: suspend (T) -> Unit) {
        scope.launch {
            collect(action)
        }
    }


    fun updateFirstName(name: String) {
        _uiState.value = _uiState.value.copy(firstName = name)
        validateFirstName(name)
        validateForm()
    }

    fun updateLastName(name: String) {
        _uiState.value = _uiState.value.copy(lastName = name)
        validateLastName(name)
        validateForm()
    }

    fun updateAddress(address: String) {
        _uiState.value = _uiState.value.copy(address = address)
        validateAddress(address)
        validateForm()
    }

    fun updatePhoneNumber(phone: String) {
        _uiState.value = _uiState.value.copy(phoneNumber = phone)
        validatePhoneNumber(phone)
        validateForm()
    }

    private fun validateFirstName(name: String) {
        _uiState.value = _uiState.value.copy(
            firstNameError = if (name.isBlank()) "Имя не может быть пустым" else null
        )
    }

    private fun validateLastName(name: String) {
        _uiState.value = _uiState.value.copy(
            lastNameError = if (name.isBlank()) "Фамилия не может быть пустой" else null
        )
    }

    private fun validateAddress(address: String) {
        _uiState.value = _uiState.value.copy(
            addressError = if (address.isBlank()) "Адрес не может быть пустым" else null
        )
    }

    private fun validatePhoneNumber(phone: String) {
        _uiState.value = _uiState.value.copy(
            phoneNumberError = when {
                phone.isBlank() -> "Телефон не может быть пустым"
                !phone.matches(Regex("^\\+?\\d{10,15}\$")) -> "Некорректный формат телефона" // Простая валидация на 10-15 цифр, опциональный +
                else -> null
            }
        )
    }

    private fun validateForm() {
        val currentState = _uiState.value
        _uiState.value = currentState.copy(
            isFormValid = currentState.firstNameError == null && currentState.lastNameError == null &&
                    currentState.addressError == null && currentState.phoneNumberError == null &&
                    currentState.firstName.isNotBlank() && currentState.lastName.isNotBlank() &&
                    currentState.address.isNotBlank() && currentState.phoneNumber.isNotBlank()
        )
    }

    fun saveProfile(onSuccess: () -> Unit) {
        val currentState = _uiState.value
        // Дополнительная валидация перед сохранением, если UIState.isFormValid не достаточно
        validateFirstName(currentState.firstName)
        validateLastName(currentState.lastName)
        validateAddress(currentState.address)
        validatePhoneNumber(currentState.phoneNumber)

        if (currentState.isFormValid) {
            viewModelScope.launch {
                val userToSave = User(
                    id = currentUser?.id ?: 0, // Используем ID существующего пользователя или 0 для нового
                    firstName = currentState.firstName,
                    lastName = currentState.lastName,
                    address = currentState.address,
                    phoneNumber = currentState.phoneNumber
                )
                if (currentUser == null) {
                    userDao.insertUser(userToSave)
                } else {
                    userDao.updateUser(userToSave)
                }
                onSuccess() // Вызываем callback для навигации
            }
        }
    }

    // ViewModel Factory для передачи зависимостей (UserDao)
    class Factory(private val userDao: UserDao) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(EditProfileViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return EditProfileViewModel(userDao) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}