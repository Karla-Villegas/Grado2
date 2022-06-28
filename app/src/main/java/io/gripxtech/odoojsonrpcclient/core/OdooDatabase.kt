package io.gripxtech.odoojsonrpcclient.core

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.gripxtech.odoojsonrpcclient.App
import io.gripxtech.odoojsonrpcclient.core.persistence.AppTypeConverters
import io.gripxtech.odoojsonrpcclient.core.userInfo.UserInfo
import io.gripxtech.odoojsonrpcclient.core.userInfo.UserInfoDao
import io.gripxtech.odoojsonrpcclient.customer.entities.Customer
import io.gripxtech.odoojsonrpcclient.customer.entities.CustomerDao

@Database(
    entities = [
        /* Add Room Entities here: BEGIN */
        Customer::class, // res.partner
        UserInfo::class,
        /* Add Room Entities here: END */
    ], version = 1 /* , exportSchema = true*/
)
@TypeConverters(AppTypeConverters::class)
abstract class OdooDatabase : RoomDatabase() {

    abstract fun customerDao(): CustomerDao
    abstract fun userInfoDao(): UserInfoDao

}
