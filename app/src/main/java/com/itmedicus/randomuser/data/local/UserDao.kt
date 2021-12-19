package com.itmedicus.randomuser.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.itmedicus.randomuser.model.AlarmTime
import com.itmedicus.randomuser.model.Dami
import com.itmedicus.randomuser.model.Name
import com.itmedicus.randomuser.model.Result

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData(userData: Result)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAlarmTime(alarmTime: AlarmTime)

    @Query("SELECT * FROM alarm_table ORDER BY id ASC  ")
    fun getAllAlarmTime(): MutableList<AlarmTime>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateDate(userData: Result)

   @Query("SELECT * FROM user_table ORDER BY id DESC LIMIT 10 ")
    fun getAllUserData(): List<Result>

    @Query("SELECT * FROM user_table ORDER BY id ASC  ")
    fun getAllUser(): List<Result>

    @Query("SELECT * FROM user_table ORDER BY id ASC  ")
    fun getAllRandomUser(): MutableList<Result>

   @Query("DELETE FROM user_table WHERE id=:id")
    suspend fun deleteItem(id: Int?)

    @Query("UPDATE user_table SET name  = :name, phone = :phone,gender =:gender," +
            "email =:email,nat=:nat,picture=:picture,location=:location  WHERE id = :id")
    fun updateQuantity(name: String,phone : String,gender : String,email : String,nat : String,picture: String,location: String,id:Int)

    @Query("SELECT * FROM user_table WHERE name LIKE :searchQuery")
    fun searchDatabase(searchQuery: String): MutableList<Result>

    @Query("SELECT id FROM user_table WHERE name = :name LIMIT 1")
    fun getUserId(name: String): Int?

    @Query("SELECT name FROM user_table ")
    fun allUserNameList(): List<Name>
}