// data/UserDao.kt
package com.example.pr35.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    // Получаем первого пользователя, если их несколько.
    // В этом приложении предполагается, что будет только один пользовательский профиль.
    @Query("SELECT * FROM users LIMIT 1")
    fun getUser(): Flow<User?> // Используем Flow для реактивного получения данных
}