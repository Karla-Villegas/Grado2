package io.gripxtech.odoojsonrpcclient.core.userInfo

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserUserInfo(userInfo: UserInfo)

    @Query("DELETE FROM `res.users`")
    suspend fun deleteUserInfo()

    @Query("SELECT * from `res.users`")
    fun getUserInfo() : LiveData<UserInfo?>

    @Query("SELECT * from `res.users`")
    suspend fun getUser() : UserInfo?

    /*@Query("UPDATE `user.info` SET price=:price WHERE id = :id")
    fun update(price: Float?, id: Int)*/
}