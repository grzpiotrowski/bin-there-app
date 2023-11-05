package ie.setu.bin_there_app.views.poi

import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import ie.setu.bin_there_app.R
import ie.setu.bin_there_app.databinding.ActivityPoiBinding
import ie.setu.bin_there_app.models.PoiModel
import timber.log.Timber.i

class PoiView : AppCompatActivity() {

    private lateinit var binding: ActivityPoiBinding
    private lateinit var presenter: PoiPresenter
    var poi = PoiModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityPoiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        presenter = PoiPresenter(this)

        binding.chooseImage.setOnClickListener {
            presenter.cachePoi(binding.poiTitle.text.toString(), binding.description.text.toString())
            presenter.doSelectImage()
        }

        binding.poiLocation.setOnClickListener {
            presenter.cachePoi(binding.poiTitle.text.toString(), binding.description.text.toString())
            presenter.doSetLocation()
        }

        binding.gpsButton.setOnClickListener {
            presenter.getCurrentLocation()
        }

        binding.btnAdd.setOnClickListener {
            if (binding.poiTitle.text.toString().isEmpty()) {
                Snackbar.make(binding.root, R.string.enter_poi_title, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                presenter.doAddOrSave(binding.poiTitle.text.toString(), binding.description.text.toString())
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_poi, menu)
        val deleteMenu: MenuItem = menu.findItem(R.id.item_delete)
        deleteMenu.isVisible = presenter.edit
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_delete -> {
                presenter.doDelete()
            }
            R.id.item_cancel -> {
                presenter.doCancel()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun showPoi(poi: PoiModel) {
        binding.poiTitle.setText(poi.title)
        binding.description.setText(poi.description)
        binding.btnAdd.setText(R.string.save_poi)
        Picasso.get()
            .load(poi.image)
            .into(binding.poiImage)
        if (poi.image != Uri.EMPTY) {
            binding.chooseImage.setText(R.string.change_poi_image)
        }
    }

    fun updateImage(image: Uri) {
        i("Image updated")
        Picasso.get()
            .load(image)
            .into(binding.poiImage)
        binding.chooseImage.setText(R.string.change_poi_image)
    }
}