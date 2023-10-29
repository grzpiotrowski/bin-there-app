package ie.setu.bin_there_app.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import ie.setu.bin_there_app.R
import ie.setu.bin_there_app.databinding.ActivityBinthereBinding
import ie.setu.bin_there_app.main.MainApp
import ie.setu.bin_there_app.models.PoiModel
import timber.log.Timber.i

class BinThereActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBinthereBinding
    var poi = PoiModel()
    lateinit var app : MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBinthereBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp
        i("BinThere Activity started.")

        binding.btnAdd.setOnClickListener() {
            poi.title = binding.poiTitle.text.toString()
            poi.description = binding.description.text.toString()
            if (poi.title.isNotEmpty()) {
                app.pois.add(poi.copy())
                i("Add Button pressed: ${poi}")
                for (i in app.pois.indices) {
                    i("POI[$i]:${this.app.pois[i]}")
                }
                setResult(RESULT_OK)
                finish()
            }
            else {
                Snackbar.make(it,"Please Enter a title", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_poi, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}