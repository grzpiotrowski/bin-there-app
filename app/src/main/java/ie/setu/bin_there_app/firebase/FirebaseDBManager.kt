package ie.setu.bin_there_app.firebase

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import ie.setu.bin_there_app.models.PoiModel
import ie.setu.bin_there_app.models.PoiStore
import timber.log.Timber

object FirebaseDBManager : PoiStore {

    var database: DatabaseReference = FirebaseDatabase.getInstance().reference

    override fun findAll(poisList: MutableLiveData<List<PoiModel>>) {
        database.child("pois")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Timber.i("Firebase POI error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val localList = ArrayList<PoiModel>()
                    val children = snapshot.children
                    children.forEach {
                        val poi = it.getValue(PoiModel::class.java)
                        localList.add(poi!!)
                    }
                    database.child("pois")
                        .removeEventListener(this)

                    poisList.value = localList
                }
            })
    }

    override fun findAll(userid: String, poisList: MutableLiveData<List<PoiModel>>) {
        database.child("user-pois").child(userid)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Timber.i("Firebase POI error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val localList = ArrayList<PoiModel>()
                    val children = snapshot.children
                    children.forEach {
                        val poi = it.getValue(PoiModel::class.java)
                        localList.add(poi!!)
                    }
                    database.child("user-pois").child(userid)
                        .removeEventListener(this)

                    poisList.value = localList
                }
            })
    }

    override fun findById(userid: String, poiid: String, poi: MutableLiveData<PoiModel>) {

        database.child("user-pois").child(userid)
            .child(poiid).get().addOnSuccessListener {
                poi.value = it.getValue(PoiModel::class.java)
                Timber.i("firebase Got value ${it.value}")
            }.addOnFailureListener{
                Timber.e("firebase Error getting data $it")
            }
    }

    override fun create(firebaseUser: MutableLiveData<FirebaseUser>, poi: PoiModel) {
        Timber.i("Firebase DB Reference : $database")

        val userid = firebaseUser.value!!.uid
        val key = database.child("pois").push().key
        if (key == null) {
            Timber.i("Firebase Error : Key Empty")
            return
        }
        poi.id = key
        poi.userId = userid
        val poiValues = poi.toMap()

        val childAdd = HashMap<String, Any>()
        childAdd["/pois/$key"] = poiValues
        childAdd["/user-pois/$userid/$key"] = poiValues

        database.updateChildren(childAdd)
    }

    override fun delete(userid: String, poiid: String) {

        val childDelete : MutableMap<String, Any?> = HashMap()
        childDelete["/pois/$poiid"] = null
        childDelete["/user-pois/$userid/$poiid"] = null

        database.updateChildren(childDelete)
    }

    override fun update(userid: String, poiid: String, poi: PoiModel) {

        val poiValues = poi.toMap()

        val childUpdate : MutableMap<String, Any?> = HashMap()
        childUpdate["pois/$poiid"] = poiValues
        childUpdate["user-pois/$userid/$poiid"] = poiValues

        database.updateChildren(childUpdate)
    }
}