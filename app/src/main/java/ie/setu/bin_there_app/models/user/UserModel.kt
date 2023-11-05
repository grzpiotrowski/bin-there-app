package ie.setu.bin_there_app.models.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(var id: Long = 0,
                    var email: String = "",
                    var password: String = "",
                    var name: String = "") : Parcelable
