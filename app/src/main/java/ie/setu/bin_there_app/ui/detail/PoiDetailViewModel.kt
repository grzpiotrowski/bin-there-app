package ie.setu.bin_there_app.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.setu.bin_there_app.models.PoiManager
import ie.setu.bin_there_app.models.PoiModel

class PoiDetailViewModel : ViewModel() {
    private val poi = MutableLiveData<PoiModel>()

    val observablePoi: LiveData<PoiModel>
        get() = poi

    fun getPoi(id: Long) {
        poi.value = PoiManager.findById(id)
    }
}