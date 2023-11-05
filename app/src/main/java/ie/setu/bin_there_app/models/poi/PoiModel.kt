package ie.setu.bin_there_app.models.poi

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PoiModel(
    var id: Long = 0,
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

) : Parcelable

enum class PoiType {
    LITTER, BIN
}

enum class LitterType {
    GENERAL, PAPER, PLASTIC, ORGANIC, METAL, OTHER, HEHEHE
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
                    var zoom: Float = 0f) : Parcelable