package ie.setu.bin_there_app.views.poi

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import timber.log.Timber.i
import ie.setu.bin_there_app.main.MainApp
import ie.setu.bin_there_app.models.PoiModel
import ie.setu.bin_there_app.databinding.ActivityPoiBinding
import ie.setu.bin_there_app.helpers.LocationHelper
import ie.setu.bin_there_app.helpers.showImagePicker
import ie.setu.bin_there_app.models.Location
import ie.setu.bin_there_app.views.editlocation.EditLocationView

class PoiPresenter(private val view: PoiView) {

    var poi = PoiModel()
    var app: MainApp = view.application as MainApp
    var binding: ActivityPoiBinding = ActivityPoiBinding.inflate(view.layoutInflater)
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    var edit = false

    init {
        if (view.intent.hasExtra("poi_edit")) {
            edit = true
            poi = view.intent.extras?.getParcelable("poi_edit")!!
            view.showPoi(poi)
        }
        registerImagePickerCallback()
        registerMapCallback()
    }

    fun doAddOrSave(title: String, description: String) {
        poi.title = title
        poi.description = description
        if (edit) {
            app.pois.update(poi)
        } else {
            app.pois.create(poi)
        }
        view.setResult(RESULT_OK)
        view.finish()
    }

    fun doCancel() {
        view.finish()
    }

    fun doDelete() {
        view.setResult(99)
        app.pois.delete(poi)
        view.finish()
    }

    fun doSelectImage() {
        showImagePicker(imageIntentLauncher,view)
    }

    fun doSetLocation() {
        val location = Location(53.174488, -6.805136, 17f)
        if (poi.location.zoom != 0f) {
            location.lat =  poi.location.lat
            location.lng = poi.location.lng
            location.zoom = poi.location.zoom
        }
        val launcherIntent = Intent(view, EditLocationView::class.java)
            .putExtra("location", location)
        mapIntentLauncher.launch(launcherIntent)
    }

    fun getCurrentLocation() {
        val permission = ContextCompat.checkSelfPermission(view, android.Manifest.permission.ACCESS_FINE_LOCATION)
        if (permission == PackageManager.PERMISSION_GRANTED) {
            fetchLocation()
        } else {
            ActivityCompat.requestPermissions(
                view,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LocationHelper.LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun fetchLocation() {
        val locationManager = view.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (hasGps) {
            if (ActivityCompat.checkSelfPermission(view, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (location != null) {
                    poi.location.lat = location.latitude
                    poi.location.lng = location.longitude
                }
            }
        } else {
            Toast.makeText(view, "Please enable Location", Toast.LENGTH_SHORT).show()
        }
    }

    fun cachePoi (title: String, description: String) {
        poi.title = title
        poi.description = description
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")
                            poi.image = result.data!!.data!!
                            view.contentResolver.takePersistableUriPermission(poi.image,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            view.updateImage(poi.image)
                        }
                    }
                    AppCompatActivity.RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Location ${result.data.toString()}")
                            val location = result.data!!.extras?.getParcelable<Location>("location")!!
                            i("Location == $location")
                            poi.location.lat = location.lat
                            poi.location.lng = location.lng
                            poi.location.zoom = location.zoom
                        }
                    }
                    AppCompatActivity.RESULT_CANCELED -> { } else -> { }
                }
            }
    }
}