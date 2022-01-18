package com.itmedicus.randomuser.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.itmedicus.randomuser.model.*

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData(userData: Result)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBmiData(bmiRecord: BmiRecord)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAlarmTime(alarmTime: AlarmTime)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMultipleAlarm(multipleAlarm: MultipleAlarm)

    @Query("SELECT * FROM alarm_table ORDER BY id ASC  ")
    fun getAllAlarmTime(): MutableList<AlarmTime>

    @Query("SELECT * FROM bmi_table ORDER BY id DESC  ")
    fun getAllBmiResult(): LiveData<MutableList<BmiRecord>>

    @Query("SELECT * FROM alarm_table ORDER BY id ASC  ")
    fun getAllAlarm(): LiveData<MutableList<AlarmTime>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertToLocal(userData: MutableList<Result>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateDate(userData: Result)

   @Query("SELECT * FROM user_table ORDER BY id DESC LIMIT 10 ")
    fun getAllUserData(): List<Result>

    @Query("SELECT bmiScore FROM bmi_table ")
    fun getBmiChartResult(): List<BmiScore>

    @Query("SELECT * FROM user_table ORDER BY id ASC  ")
    fun getAllUser(): List<Result>

    @Query("SELECT * FROM user_table ORDER BY id ASC  ")
    fun getAllRandomUser(): MutableList<Result>

   @Query("DELETE FROM user_table WHERE id=:id")
    suspend fun deleteItem(id: Int?)

    @Query("UPDATE user_table SET name  = :name, phone = :phone,gender =:gender," +
            "email =:email,nat=:nat,picture=:picture,location=:location  WHERE id = :id")
    fun updateQuantity(name: String,phone : String,gender : String,email : String,nat : String,picture: String,location: String,id:Int)

    @Query("UPDATE alarm_table SET flag =:flag WHERE id =:id")
    fun updateSwitchButtonState(flag:Boolean,id: Long)


    @Query("UPDATE alarm_table SET calenderTime =:time WHERE id =:id")
    fun updateAlarmTime(time:Long,id: Long)

    @Query("UPDATE multiple_alarm_table SET calenderTime =:time WHERE fk_id =:id")
    fun updateAlarmTimeInSpecificDay(time:Long,id: Long)

    @Query("UPDATE multiple_alarm_table SET calenderTime =:time WHERE id =:id")
    fun updateAlarmTimeInMultipleDay(time:Long,id: Int)

    @Query("SELECT * FROM user_table WHERE name LIKE :searchQuery")
    fun searchDatabase(searchQuery: String): MutableList<Result>

    @Query("SELECT id FROM user_table WHERE name = :name LIMIT 1")
    fun getUserId(name: String): Int?

    @Query("SELECT name FROM user_table ")
    fun allUserNameList(): List<Name>

    @Delete
    suspend fun deleteAlarm(alarmTime: AlarmTime)

    @Delete
    suspend fun deleteBmiData(bmiRecord: BmiRecord)

    @Query("DELETE FROM multiple_alarm_table WHERE fk_id = :fk_id")
    suspend fun deleteMultipleAlarm(fk_id : Long)

    @Transaction
    @Query("SELECT * FROM alarm_table WHERE time = :time")
    fun getAlarmWithMultipleAlarm(time : String): List<AlarmTimeAndMultipleAlarm>

    @Query("SELECT * FROM multiple_alarm_table WHERE fk_id = :fk_id")
    fun getAlarmRequestCode(fk_id : Long): List<RequestCode>

    @Query("SELECT * FROM multiple_alarm_table ORDER BY id DESC")
    fun getAlarmId(): MutableList<RequestCode>
}