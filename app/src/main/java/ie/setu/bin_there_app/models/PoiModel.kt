package ie.setu.bin_there_app.models

import android.net.Uri
import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize

@IgnoreExtraProperties
@Parcelize
data class PoiModel(
    var id: String? = "",
    var userId: Long = 0,
    var poiType: PoiType = PoiType.LITTER,
    var title: String = "",
    var description: String = "",
    var image: Uri = Uri.EMPTY,
    var location: Location = Location(),
    var dateReported: Long = System.currentTimeMillis(),
    var isCleanedUp: Boolean = false,
    var litterType: LitterType? = null,
    var binStatus: BinStatus? = null,
    var binType: BinType? = null,

    ) : Parcelable {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "userId" to userId,
            "poiType" to poiType.name,
            "title" to title,
            "description" to description,
            "location" to location.toMap(),
            "dateReported" to dateReported,
            "isCleanedUp" to isCleanedUp,
            "litterType" to litterType?.name,
            "binStatus" to binStatus?.name,
            "binType" to binType?.name
        )
    }
}

enum class PoiType {
    LITTER, BIN
}

enum class LitterType {
    GENERAL, PAPER, PLASTIC, ORGANIC, METAL, OTHER
}

enum class BinStatus {
    NORMAL, OVERFLOWING, SUGGESTED_NEW_LOCATION
}

enum class BinType {
    GENERAL, RECYCLABLES, ORGANIC, ELECTRONIC, GLASS, OTHER
}

@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "lat" to lat,
            "lng" to lng,
            "zoom" to zoom
        )
    }
}