package ie.setu.bin_there_app.ui.addpoi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.setu.bin_there_app.models.PoiManager
import ie.setu.bin_there_app.models.PoiModel

class AddPoiViewModel : ViewModel() {

    private val status = MutableLiveData<Boolean>()

    val observableStatus: LiveData<Boolean>
        get() = status

    fun addPoi(poi: PoiModel) {
        status.value = try {
            PoiManager.create(poi)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }
}