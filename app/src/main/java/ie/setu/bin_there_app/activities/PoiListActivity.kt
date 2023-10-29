package ie.setu.bin_there_app.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ie.setu.bin_there_app.R
import ie.setu.bin_there_app.main.MainApp

class PoiListActivity : AppCompatActivity() {

    lateinit var app: MainApp
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poi_list)
        app = application as MainApp
    }
}