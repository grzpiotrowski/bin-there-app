package ie.setu.bin_there_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.ajalt.timberkt.Timber
import timber.log.Timber.i

class BinThereActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_binthere)

        Timber.plant(Timber.DebugTree())
        i("BinThere Activity started..")
    }
}