package ie.setu.bin_there_app.views.map

import androidx.core.graphics.toColor
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import ie.setu.bin_there_app.R
import ie.setu.bin_there_app.main.MainApp
import ie.setu.bin_there_app.models.poi.PoiType

class PoiMapPresenter(val view: PoiMapView) {
    var app: MainApp

    init {
        app = view.application as MainApp
    }

    fun doPopulateMap(map: GoogleMap) {
        map.uiSettings.setZoomControlsEnabled(true)
        map.setOnMarkerClickListener(view)
        map.setOnMapClickListener { latLng ->
            view.showAddPoiDialog(latLng, map)
        }

        app.pois.findAll().forEach {
            val loc = LatLng(it.location.lat, it.location.lng)
            val options = MarkerOptions().title(it.title).position(loc)

            // Set icon based on PoiType and CleanedUp status
            when {
                it.poiType == PoiType.LITTER && !it.isCleanedUp ->
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))

                it.poiType == PoiType.LITTER && it.isCleanedUp ->
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))

                it.poiType == PoiType.BIN ->
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
            }

            map.addMarker(options)?.tag = it.id
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.location.zoom))
        }
    }

    fun doMarkerSelected(marker: Marker) {
        val tag = marker.tag as Long
        val poi = app.pois.findById(tag)
        if (poi != null) view.showPoi(poi)
    }
}