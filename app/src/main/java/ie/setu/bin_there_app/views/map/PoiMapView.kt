package ie.setu.bin_there_app.views.map

import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Picasso
import ie.setu.bin_there_app.R
import ie.setu.bin_there_app.databinding.ActivityPoiMapsBinding
import ie.setu.bin_there_app.databinding.ContentPoiMapsBinding
import ie.setu.bin_there_app.helpers.LocationHelper
import ie.setu.bin_there_app.main.MainApp
import ie.setu.bin_there_app.models.Location
import ie.setu.bin_there_app.models.PoiModel

class PoiMapView : AppCompatActivity(), GoogleMap.OnMarkerClickListener,
    GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener {

    private lateinit var binding: ActivityPoiMapsBinding
    private lateinit var contentBinding: ContentPoiMapsBinding
    lateinit var app: MainApp
    lateinit var presenter: PoiMapPresenter
    private lateinit var locationHelper: LocationHelper

    private var googleMap: GoogleMap? = null

    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private var permissionDenied = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = application as MainApp
        binding = ActivityPoiMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        presenter = PoiMapPresenter(this)

        contentBinding = ContentPoiMapsBinding.bind(binding.root)
        contentBinding.mapView.onCreate(savedInstanceState)

        locationHelper = LocationHelper(this)
        contentBinding.mapView.getMapAsync{
            googleMap = it
            presenter.doPopulateMap(it)
            it.setOnMyLocationButtonClickListener(this)
            it.setOnMyLocationClickListener(this)
            locationHelper.enableMyLocation(it)
        }
    }

    fun showAddPoiDialog(latLng: LatLng, map: GoogleMap) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_poi, null)
        val titleEditText: EditText = dialogView.findViewById(R.id.titleEditText)
        val descriptionEditText: EditText = dialogView.findViewById(R.id.descriptionEditText)

        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Add New POI")
            .setView(dialogView)
            .setPositiveButton("Add", null)
            .setNegativeButton("Cancel", null)
            .create()

        alertDialog.setOnShowListener {
            val positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setOnClickListener {
                val title = titleEditText.text.toString()
                val description = descriptionEditText.text.toString()

                if (title.isNotEmpty() && description.isNotEmpty()) {
                    val newPoi = PoiModel(title=title,
                        description=description,
                        location=Location(latLng.latitude, latLng.longitude, 17f))
                    app.pois.create(newPoi.copy())
                    val options = MarkerOptions().title(title).position(latLng)
                    map.addMarker(options)?.tag = newPoi.id
                    setResult(RESULT_OK)
                    alertDialog.dismiss()
                } else {
                    Toast.makeText(this, "Please fill in all details", Toast.LENGTH_SHORT).show()
                    // The dialog won't close. Handling the button click manually.
                }
            }
        }
        alertDialog.show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (!locationHelper.onRequestPermissionsResult(requestCode, grantResults, googleMap)) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    fun showPoi(poi: PoiModel) {
        contentBinding.currentTitle.text = poi.title
        contentBinding.currentDescription.text = poi.description
        Picasso.get()
            .load(poi.image)
            .into(contentBinding.currentImage)
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        presenter.doMarkerSelected(marker)
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        contentBinding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        contentBinding.mapView.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        contentBinding.mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        contentBinding.mapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        contentBinding.mapView.onSaveInstanceState(outState)
    }

    override fun onMyLocationClick(location: android.location.Location) {
        locationHelper.showCurrentLocationToast(location)
    }

    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(this, "Your current location", Toast.LENGTH_SHORT).show()
        return false
    }

}