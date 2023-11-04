package ie.setu.bin_there_app.views.map

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.squareup.picasso.Picasso
import ie.setu.bin_there_app.databinding.ActivityPoiMapsBinding
import ie.setu.bin_there_app.databinding.ContentPoiMapsBinding
import ie.setu.bin_there_app.main.MainApp
import ie.setu.bin_there_app.models.PoiModel

class PoiMapView : AppCompatActivity(), GoogleMap.OnMarkerClickListener {

    private lateinit var binding: ActivityPoiMapsBinding
    private lateinit var contentBinding: ContentPoiMapsBinding
    lateinit var app: MainApp
    lateinit var presenter: PoiMapPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = application as MainApp
        binding = ActivityPoiMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        presenter = PoiMapPresenter(this)

        contentBinding = ContentPoiMapsBinding.bind(binding.root)
        contentBinding.mapView.onCreate(savedInstanceState)

        contentBinding.mapView.getMapAsync{
            presenter.doPopulateMap(it)
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
}