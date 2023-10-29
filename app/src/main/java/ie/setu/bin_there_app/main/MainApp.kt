package ie.setu.bin_there_app.main

import android.app.Application
import com.github.ajalt.timberkt.Timber
import ie.setu.bin_there_app.models.PoiModel
import timber.log.Timber.i

class MainApp : Application () {

    var pois = ArrayList<PoiModel>()

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        i("BinThere started")
    }
}