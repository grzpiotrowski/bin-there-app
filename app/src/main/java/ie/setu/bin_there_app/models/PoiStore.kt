package ie.setu.bin_there_app.models

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser

interface PoiStore {
    fun findAll(poisList:
                MutableLiveData<List<PoiModel>>
    )
    fun findAll(userid:String,
                poisList:
                MutableLiveData<List<PoiModel>>)
    fun findById(userid:String, poiid: String,
                 poi: MutableLiveData<PoiModel>)
    fun create(firebaseUser: MutableLiveData<FirebaseUser>, poi: PoiModel)
    fun delete(userid:String, poiid: String)
    fun update(userid:String, poiid: String, poi: PoiModel)
}