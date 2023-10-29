package ie.setu.bin_there_app.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PoiModel(var id: Long = 0,
                    var title: String = "",
                    var description: String = "") : Parcelable
