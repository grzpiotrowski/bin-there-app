package ie.setu.bin_there_app.activities

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Picasso
import ie.setu.bin_there_app.R
import ie.setu.bin_there_app.databinding.ActivityPoiMapsBinding
import ie.setu.bin_there_app.databinding.ContentPoiMapsBinding
import ie.setu.bin_there_app.main.MainApp
import ie.setu.bin_there_app.models.Location
import ie.setu.bin_there_app.models.PoiModel

class PoiMapsActivity : AppCompatActivity(), GoogleMap.OnMarkerClickListener {

    private lateinit var binding: ActivityPoiMapsBinding
    private lateinit var contentBinding: ContentPoiMapsBinding
    lateinit var map: GoogleMap
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPoiMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        app = application as MainApp
        contentBinding = ContentPoiMapsBinding.bind(binding.root)
        contentBinding.mapView.onCreate(savedInstanceState)

        contentBinding.mapView.getMapAsync {
            map = it
            configureMap()
        }
    }
    private fun configureMap() {
        map.uiSettings.isZoomControlsEnabled = true
        app.pois.findAll().forEach {
            val loc = LatLng(it.location.lat, it.location.lng)
            val options = MarkerOptions().title(it.title).position(loc)
            map.addMarker(options)?.tag = it.id
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.location.zoom))
        }
        map.setOnMarkerClickListener(this)

        map.setOnMapClickListener { latLng ->
            showAddPoiDialog(latLng)
        }
    }

    private fun showAddPoiDialog(latLng: LatLng) {
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
                    val newPoi = PoiModel(title=title, description=description, location=Location(latLng.latitude, latLng.longitude, 17f))
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

    override fun onMarkerClick(marker: Marker): Boolean {
        val tag = marker.tag as Long
        val poi = app.pois.findById(tag)
        contentBinding.currentTitle.text = poi!!.title
        contentBinding.currentDescription.text = poi.description
        Picasso.get().load(poi.image).into(contentBinding.currentImage)
        return false
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
}