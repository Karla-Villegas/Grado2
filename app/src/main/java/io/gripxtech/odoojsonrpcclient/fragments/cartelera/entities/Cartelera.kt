package io.gripxtech.odoojsonrpcclient.fragments.cartelera.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "ev_cartelera", primaryKeys = ["_id", "server_id"])
data class Cartelera(
    @ColumnInfo(name = "_id")
    var _id: Long = 0,

    @Expose
    @SerializedName("id")
    @ColumnInfo(name = "server_id")
    var serverId: Long = 0,

    @Expose
    @SerializedName("title")
    @ColumnInfo(name = "title")
    var title: String,

    @Expose
    @SerializedName("description")
    @ColumnInfo(name = "description")
    var description: String,

    @Expose
    @SerializedName("image")
    @ColumnInfo(name = "image")
    var image: String,

    @Expose
    @SerializedName("image_url")
    @ColumnInfo(name = "image_url")
    var image_url: String,

    @Expose
    @SerializedName("date")
    @ColumnInfo(name = "date")
    var date: String,

    @Expose
    @SerializedName("expiry_date")
    @ColumnInfo(name = "expiry_date")
    var expiry_date: String


): Parcelable {
    companion object {
        @JvmField
        val fieldsMap: Map<String, String> = mapOf(
            "_id" to "_id",
            "serverId" to "serverId",
            "title" to "title",
            "description" to "description",
            "image" to "image",
            "image_url" to "image_url",
            "date" to "date",
            "expiry_date" to "expiry_date"

        )

        @JvmField
        val fields: ArrayList<String> = fieldsMap.keys.toMutableList() as ArrayList<String>
    }

}
