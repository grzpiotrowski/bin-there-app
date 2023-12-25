package ie.setu.bin_there_app.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.setu.bin_there_app.firebase.FirebaseDBManager
import ie.setu.bin_there_app.models.PoiModel
import timber.log.Timber

class PoiDetailViewModel : ViewModel() {
    private val poi = MutableLiveData<PoiModel>()

    val observablePoi: LiveData<PoiModel>
        get() = poi

    fun getPoi(userid: String, id: String) {
        try {
            FirebaseDBManager.findById(userid, id, poi)
            Timber.i("Detail getPoi() Success : ${
                poi.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Detail getPoi() Error : $e.message")
        }
    }

    fun cleanupPoi(userid: String, id: String, poi: PoiModel) {
        try {
            poi.isCleanedUp = true
            FirebaseDBManager.update(userid, id, poi)
            Timber.i("CleanupPOI Success : $poi")
        }
        catch (e: Exception) {
            Timber.i("CleanupPOI Error : $e.message")
        }
    }
}