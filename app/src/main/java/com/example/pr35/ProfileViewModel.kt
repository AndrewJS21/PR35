package com.example.pr35

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.yourcompany.pr35.data.User
import com.yourcompany.pr35.data.UserDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(private val userDao: UserDao) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    init {
        // Подписываемся на изменения пользователя из базы данных
        viewModelScope.launch {
            userDao.getUser().collect { fetchedUser ->
                _user.value = fetchedUser
                // Если пользователя нет, вставляем данные по умолчанию (для первого запуска)
                if (fetchedUser == null) {
                    val defaultUser = User(
                        firstName = "Emmanuel",
                        lastName = "Oyiboke",
                        address = "Nigeria",
                        phoneNumber = "+7 811-732-5298"
                    )
                    userDao.insertUser(defaultUser)
                }
            }
        }
    }

    // ViewModel Factory для передачи зависимостей (UserDao)
    class Factory(private val userDao: UserDao) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ProfileViewModel(userDao) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}