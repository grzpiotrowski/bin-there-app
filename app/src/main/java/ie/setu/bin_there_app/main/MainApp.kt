package ie.setu.bin_there_app.main

import android.app.Application
import timber.log.Timber

class MainApp : Application() {

    //lateinit var poisStore: PoiStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
      //  poisStore = PoiManager()
        Timber.i("BinThere Application Started")
    }
}