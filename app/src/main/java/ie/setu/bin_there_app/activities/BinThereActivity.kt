package ie.setu.bin_there_app.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.ajalt.timberkt.Timber
import com.google.android.material.snackbar.Snackbar
import ie.setu.bin_there_app.databinding.ActivityBinthereBinding
import timber.log.Timber.i

class BinThereActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBinthereBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBinthereBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Timber.plant(Timber.DebugTree())
        i("BinThere Activity started..")

        binding.btnAdd.setOnClickListener() {
            val poiTitle = binding.poiTitle.text.toString()
            if (poiTitle.isNotEmpty()) {
                i("add Button Pressed: $poiTitle")
            }
            else {
                Snackbar
                    .make(it,"Please Enter a title", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }
}