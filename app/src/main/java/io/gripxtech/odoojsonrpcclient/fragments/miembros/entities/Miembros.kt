package io.gripxtech.odoojsonrpcclient.fragments.miembros.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.gripxtech.odoojsonrpcclient.core.utils.JsonElementParceler
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.TypeParceler

@Parcelize
@Entity(tableName = "ev_believer", primaryKeys = ["_id", "server_id"])
data class Miembros(

    @Expose
    @SerializedName("_id")
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
    @SerializedName("identity")
    @ColumnInfo(name = "identity")
    var identity: String,

    @Expose
    @SerializedName("state")
    @ColumnInfo(name = "state")
    @TypeParceler<JsonElement, JsonElementParceler>
    var state: JsonElement = JsonArray(),

    @Expose
    @SerializedName("municipality")
    @ColumnInfo(name = "municipality")
    @TypeParceler<JsonElement, JsonElementParceler>
    var municipality: JsonElement = JsonArray(),

    @Expose
    @SerializedName("parish")
    @ColumnInfo(name = "parish")
    @TypeParceler<JsonElement, JsonElementParceler>
    var parish: JsonElement = JsonArray(),

    @Expose
    @SerializedName("sector")
    @ColumnInfo(name = "sector")
    var sector: String,

    @Expose
    @SerializedName("street")
    @ColumnInfo(name = "street")
    var street: String,

    @Expose
    @SerializedName("building")
    @ColumnInfo(name = "building")
    var building: String,

    @Expose
    @SerializedName("house")
    @ColumnInfo(name = "house")
    var house: String,

    @Expose
    @SerializedName("localphone_number")
    @ColumnInfo(name = "localphone_number")
    var localphone_number: String

    /*@Expose
    @SerializedName("department_ids")
    @ColumnInfo(name = "department_ids")
    var department_ids: String*/


    ): Parcelable {
    companion object {
        @JvmField
        val fieldsMap: Map<String, String> = mapOf(
            "id" to "id",
            "serverId" to "serverId",
            "name" to "name",
            "identity" to "identity",
            "state" to "state",
            "municipality" to "municipality",
            "parish" to "parish",
            "sector" to "sector",
            "street" to "street",
            "building" to "building",
            "house" to "house",
            "localphone_number" to "localphone_number"
          /*  "department_ids" to "department_ids"*/

        )

        @JvmField
        val fields: ArrayList<String> = fieldsMap.keys.toMutableList() as ArrayList<String>
    }
}
