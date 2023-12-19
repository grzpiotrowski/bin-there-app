package ie.setu.bin_there_app.ui.poilist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.setu.bin_there_app.models.PoiManager
import ie.setu.bin_there_app.models.PoiModel

class PoiListViewModel : ViewModel() {

    private val poiList = MutableLiveData<List<PoiModel>>()

    val observablePoisList: LiveData<List<PoiModel>>
        get() = poiList

    init {
        load()
    }

    fun load() {
        poiList.value = PoiManager.findAll()
    }
}