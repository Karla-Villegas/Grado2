package io.gripxtech.odoojsonrpcclient.fragments.miembros.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "tc.partner", primaryKeys = ["_id", "server_id"])
data class Miembros(
    @ColumnInfo(name = "_id")
    var _id: Long = 0,

    @Expose
    @SerializedName("id")
    @ColumnInfo(name = "server_id")
    var serverId: Long = 0,

    @Expose
    @SerializedName("name")
    @ColumnInfo(name = "name")
    var name: String,

    @Expose
    @SerializedName("street")
    @ColumnInfo(name = "street")
    var street: String,

    @Expose
    @SerializedName("email")
    @ColumnInfo(name = "email")
    var email: String,

    @Expose
    @SerializedName("phone")
    @ColumnInfo(name = "phone")
    var phone: String,




): Parcelable {
    companion object {
        @JvmField
        val fieldsMap: Map<String, String> = mapOf(
            "id" to "id",
            "serverId" to "serverId",
            "name" to "name",
            "street" to "street",
            "email" to "email",
            "phone" to "phone"

        )

        @JvmField
        val fields: ArrayList<String> = fieldsMap.keys.toMutableList() as ArrayList<String>
    }
}
