package ie.setu.bin_there_app.views.poilist

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ie.setu.bin_there_app.R
import ie.setu.bin_there_app.adapters.PoiAdapter
import ie.setu.bin_there_app.adapters.PoiListener
import ie.setu.bin_there_app.databinding.ActivityPoiListBinding
import ie.setu.bin_there_app.main.MainApp
import ie.setu.bin_there_app.models.PoiModel

class PoiListView : AppCompatActivity(), PoiListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityPoiListBinding
    lateinit var presenter: PoiListPresenter
    private var position: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPoiListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)
        presenter = PoiListPresenter(this)
        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        loadPois()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> { presenter.doAddPoi() }
            R.id.item_map -> { presenter.doShowPoisMap() }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPoiClick(poi: PoiModel, position: Int) {
        this.position = position
        presenter.doEditPoi(poi, this.position)
    }

    private fun loadPois() {
        binding.recyclerView.adapter = PoiAdapter(presenter.getPois(), this)
        onRefresh()
    }

    fun onRefresh() {
        binding.recyclerView.adapter?.
        notifyItemRangeChanged(0,presenter.getPois().size)
    }

    fun onDelete(position : Int) {
        binding.recyclerView.adapter?.notifyItemRemoved(position)
    }
}