package ie.setu.bin_there_app.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.ajalt.timberkt.Timber
import com.google.android.material.snackbar.Snackbar
import ie.setu.bin_there_app.databinding.ActivityBinthereBinding
import ie.setu.bin_there_app.models.PoiModel
import timber.log.Timber.i

class BinThereActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBinthereBinding
    var poi = PoiModel()
    var pois = ArrayList<PoiModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBinthereBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Timber.plant(Timber.DebugTree())
        i("BinThere Activity started..")

        binding.btnAdd.setOnClickListener() {
            poi.title = binding.poiTitle.text.toString()
            poi.description = binding.description.text.toString()
            if (poi.title.isNotEmpty()) {
                pois.add(poi.copy())
                i("add Button Pressed: ${poi}")
                for (i in pois.indices)
                { i("Poi[$i]:${this.pois[i]}") }
            }
            else {
                Snackbar.make(it,"Please Enter a title", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }
}