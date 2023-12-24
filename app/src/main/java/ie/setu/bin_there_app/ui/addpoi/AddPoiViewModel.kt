package ie.setu.bin_there_app.ui.addpoi

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.setu.bin_there_app.firebase.FirebaseDBManager
import ie.setu.bin_there_app.firebase.FirebaseImageManager
import ie.setu.bin_there_app.models.PoiModel

class AddPoiViewModel : ViewModel() {

    private val status = MutableLiveData<Boolean>()
    private val _poiImageUri = MutableLiveData<Uri>()
    val poiImageUri: LiveData<Uri> = _poiImageUri

    val observableStatus: LiveData<Boolean>
        get() = status

    fun onImageSelected(uri: Uri) {
        _poiImageUri.value = uri
    }

    fun addPoi(firebaseUser: MutableLiveData<FirebaseUser>,
               poi: PoiModel, imageUri: Uri?) {
        status.value = try {
            poi.image = imageUri.toString()
            FirebaseDBManager.create(firebaseUser,poi)
            FirebaseImageManager.uploadImageToFirebase(poi.id!!, poiImageUri.value!!)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }
}