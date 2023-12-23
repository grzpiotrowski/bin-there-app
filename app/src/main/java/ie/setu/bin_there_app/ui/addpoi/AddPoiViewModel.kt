package ie.setu.bin_there_app.ui.addpoi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.setu.bin_there_app.firebase.FirebaseDBManager
import ie.setu.bin_there_app.models.PoiModel

class AddPoiViewModel : ViewModel() {

    private val status = MutableLiveData<Boolean>()

    val observableStatus: LiveData<Boolean>
        get() = status

    fun addPoi(firebaseUser: MutableLiveData<FirebaseUser>,
               poi: PoiModel) {
        status.value = try {
            FirebaseDBManager.create(firebaseUser,poi)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }
}