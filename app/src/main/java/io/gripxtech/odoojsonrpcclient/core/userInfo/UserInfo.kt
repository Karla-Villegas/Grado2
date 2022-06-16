package io.gripxtech.odoojsonrpcclient.core.userInfo

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

@Entity(tableName = "res.users", primaryKeys = ["_id", "server_id"])
@Parcelize
data class UserInfo(
    @ColumnInfo(name = "_id")
    var _id: Int = 0,

    @Expose
    @SerializedName("id")
    @ColumnInfo(name = "server_id")
    var serverId: Int = 0,

    @Expose
    @SerializedName("partner_id")
    @ColumnInfo(name = "partner_id")
    @TypeParceler<JsonElement, JsonElementParceler>
    var partner_id: JsonElement = JsonArray(),

    @Expose
    @SerializedName("name")
    @ColumnInfo(name = "name")
    var name: String?,

    @Expose
    @SerializedName("email")
    @ColumnInfo(name = "email")
    var email: String?,

    @Expose
    @SerializedName("phone")
    @ColumnInfo(name = "phone")
    var phone: String?,

    @Expose
    @SerializedName("image")
    @ColumnInfo(name = "image")
    var image: String?,

    @Expose
    @SerializedName("state_id")
    @ColumnInfo(name = "state_id")
    @TypeParceler<JsonElement, JsonElementParceler>
    var state_id: JsonElement = JsonArray(),

    @Expose
    @SerializedName("municipality_id")
    @ColumnInfo(name = "municipality_id")
    @TypeParceler<JsonElement, JsonElementParceler>
    var municipality_id: JsonElement = JsonArray(),

    @Expose
    @SerializedName("parish_id")
    @ColumnInfo(name = "parish_id")
    @TypeParceler<JsonElement, JsonElementParceler>
    var parish_id: JsonElement = JsonArray(),

    @Expose
    @SerializedName("country_id")
    @ColumnInfo(name = "country_id")
    @TypeParceler<JsonElement, JsonElementParceler>
    var country_id: JsonElement = JsonArray()

): Parcelable {
    companion object {
        @JvmField
        val fieldsMap: Map<String, String> = mapOf(
            "_id" to "_id",
            "serverId" to "serverId",
            "partner_id" to "partner_id",
            "name" to "name",
            "email" to "email",
            "phone" to "phone",
            "image" to "image",
            "state_id" to "state_id",
            "municipality_id" to "municipality_id",
            "parish_id" to "parish_id",
            "country_id" to "country_id"
        )
        @JvmField
        val fields: ArrayList<String> = fieldsMap.keys.toMutableList() as ArrayList<String>
    }
}
