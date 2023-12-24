package ie.setu.bin_there_app.ui.poilist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.setu.bin_there_app.firebase.FirebaseDBManager
import ie.setu.bin_there_app.models.PoiModel
import timber.log.Timber
import java.lang.Exception

class PoiListViewModel : ViewModel() {

    private val poiList = MutableLiveData<List<PoiModel>>()
    var readOnly = MutableLiveData(false)

    val observablePoisList: LiveData<List<PoiModel>>
        get() = poiList

    var liveFirebaseUser = MutableLiveData<FirebaseUser>()

    init {
        load()
    }

    fun load() {
        try {
            readOnly.value = false
            FirebaseDBManager.findAll(liveFirebaseUser.value?.uid!!,poiList)
            Timber.i("POIList Load Success : ${poiList.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("POIList Load Error : $e.message")
        }
    }

    fun loadAll() {
        try {
            readOnly.value = true
            FirebaseDBManager.findAll(poiList)
            Timber.i("POIList LoadAll Success : ${poiList.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("POIList LoadAll Error : $e.message")
        }
    }

    fun delete(userid: String, id: String) {
        try {
            FirebaseDBManager.delete(userid,id)
            Timber.i("POIList Delete Success")
        }
        catch (e: Exception) {
            Timber.i("POIList Delete Error : $e.message")
        }
    }
}