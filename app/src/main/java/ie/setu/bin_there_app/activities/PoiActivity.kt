package ie.setu.bin_there_app.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import ie.setu.bin_there_app.R
import ie.setu.bin_there_app.databinding.ActivityBinthereBinding
import ie.setu.bin_there_app.helpers.showImagePicker
import ie.setu.bin_there_app.main.MainApp
import ie.setu.bin_there_app.models.Location
import ie.setu.bin_there_app.models.PoiModel
import timber.log.Timber.i

class PoiActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBinthereBinding
    var poi = PoiModel()
    lateinit var app : MainApp
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    var edit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        edit = false

        binding = ActivityBinthereBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp
        i("BinThere Activity started.")

        if (intent.hasExtra("poi_edit")) {
            edit = true
            poi = intent.extras?.getParcelable("poi_edit")!!
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

        binding.btnAdd.setOnClickListener() {
            poi.title = binding.poiTitle.text.toString()
            poi.description = binding.description.text.toString()
            if (poi.title.isEmpty()) {
                Snackbar.make(it, R.string.enter_poi_title, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                if (edit) {
                    app.pois.update(poi.copy())
                } else {
                    app.pois.create(poi.copy())
                }
            }
            i("Add Button pressed: $poi")
            setResult(RESULT_OK)
            finish()
        }

        binding.chooseImage.setOnClickListener {
            i("Select image pressed")
            showImagePicker(imageIntentLauncher, this)
        }
        registerImagePickerCallback()

        binding.poiLocation.setOnClickListener {
            i ("Set Location pressed")
            val location = Location(53.174488, -6.805136, 17f)
            if (poi.location.zoom != 0f) {
                location.lat = poi.location.lat
                location.lng = poi.location.lng
                location.zoom = poi.location.zoom
            }
            val launcherIntent = Intent(this, MapActivity::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
        }
        registerMapCallback()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_poi, menu)
        if (edit) menu.getItem(0).isVisible = true
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_delete -> {
                setResult(99)
                app.pois.delete(poi)
                finish()
            }
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")

                            val image = result.data!!.data!!
                            contentResolver.takePersistableUriPermission(image,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            poi.image = image

                            Picasso.get()
                                .load(poi.image)
                                .into(binding.poiImage)
                            binding.chooseImage.setText(R.string.change_poi_image)
                        }
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Location ${result.data.toString()}")
                            val location = result.data!!.extras?.getParcelable<Location>("location")!!
                            i("Location == $location")
                            poi.location.lat = location.lat
                            poi.location.lng = location.lng
                            poi.location.zoom = location.zoom
                        }
                    }
                    RESULT_CANCELED -> {}
                    else -> {}
                }
            }
    }

}