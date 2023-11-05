package ie.setu.bin_there_app.main

import android.app.Application
import com.github.ajalt.timberkt.Timber
import ie.setu.bin_there_app.models.poi.PoiJSONStore
import ie.setu.bin_there_app.models.poi.PoiStore
import timber.log.Timber.i

class MainApp : Application () {

    lateinit var pois: PoiStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        pois = PoiJSONStore(applicationContext)
        //pois = PoiMemStore()
        i("BinThere started")
    }
}