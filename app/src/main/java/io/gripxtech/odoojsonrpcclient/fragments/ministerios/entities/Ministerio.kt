package io.gripxtech.odoojsonrpcclient.fragments.ministerios.entities

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
@Entity(tableName = "ev_ministerio", primaryKeys = ["id"])
data class Ministerio(

    @Expose
    @SerializedName("id")
    @ColumnInfo(name = "id")
    var serverId: Long = 0,

    @Expose
    @SerializedName("name")
    @ColumnInfo(name = "name")
    var name: String,


    @Expose
    @SerializedName("believer_ids")
    @ColumnInfo(name = "believer_ids")
    @TypeParceler<JsonElement, JsonElementParceler>
    var believer_ids: JsonElement = JsonArray()

) : Parcelable {

    companion object {
        @JvmField
        val fieldsMap: Map<String, String> = mapOf(
            "id" to "id",
            "name" to "name",
            "believer_id" to "believer_id"
        )

        @JvmField
        val fields: ArrayList<String> = fieldsMap.keys.toMutableList() as ArrayList<String>
    }
}