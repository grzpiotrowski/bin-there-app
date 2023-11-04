package ie.setu.bin_there_app.views.poilist

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import ie.setu.bin_there_app.views.poi.PoiView
import ie.setu.bin_there_app.main.MainApp
import ie.setu.bin_there_app.models.PoiModel
import ie.setu.bin_there_app.views.editlocation.EditLocationView
import ie.setu.bin_there_app.views.map.PoiMapView

class PoiListPresenter(val view: PoiListView) {

    var app: MainApp
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    private var position: Int = 0

    init {
        app = view.application as MainApp
        registerMapCallback()
        registerRefreshCallback()
    }

    fun getPois() = app.pois.findAll()

    fun doAddPoi() {
        val launcherIntent = Intent(view, PoiView::class.java)
        refreshIntentLauncher.launch(launcherIntent)
    }

    fun doEditPoi(poi: PoiModel, pos: Int) {
        val launcherIntent = Intent(view, PoiView::class.java)
        launcherIntent.putExtra("poi_edit", poi)
        position = pos
        refreshIntentLauncher.launch(launcherIntent)
    }

    fun doShowPoisMap() {
        val launcherIntent = Intent(view, PoiMapView::class.java)
        mapIntentLauncher.launch(launcherIntent)
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            view.registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) {
                if (it.resultCode == Activity.RESULT_OK) view.onRefresh()
                else // Deleting
                    if (it.resultCode == 99) view.onDelete(position)
            }
    }
    private fun registerMapCallback() {
        mapIntentLauncher = view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { if (it.resultCode == Activity.RESULT_OK) view.onRefresh() }
    }
}