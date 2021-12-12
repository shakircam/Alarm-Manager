package com.itmedicus.randomuser.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.itmedicus.randomuser.model.Dami
import com.itmedicus.randomuser.model.Result

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData(userData: ArrayList<Result>)

    @Query("SELECT * FROM user_table ORDER BY id ASC")
    fun getAllUserData(): List<Result>
}