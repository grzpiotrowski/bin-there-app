package ie.setu.bin_there_app.views.poi

import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import ie.setu.bin_there_app.R
import ie.setu.bin_there_app.databinding.ActivityPoiBinding
import ie.setu.bin_there_app.models.poi.PoiModel
import ie.setu.bin_there_app.models.poi.PoiType
import ie.setu.bin_there_app.models.poi.LitterType
import ie.setu.bin_there_app.models.poi.BinStatus
import ie.setu.bin_there_app.models.poi.BinType
import timber.log.Timber.i

class PoiView : AppCompatActivity() {

    private lateinit var binding: ActivityPoiBinding
    private lateinit var presenter: PoiPresenter
    var poi = PoiModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPoiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        presenter = PoiPresenter(this)

        binding.chooseImage.setOnClickListener {
            presenter.cachePoi(binding.poiTitle.text.toString(), binding.description.text.toString())
            presenter.doSelectImage()
        }

        binding.poiLocation.setOnClickListener {
            presenter.cachePoi(binding.poiTitle.text.toString(), binding.description.text.toString())
            presenter.doSetLocation()
        }

        binding.gpsButton.setOnClickListener {
            presenter.getCurrentLocation()
        }

        // Set up the spinners with the enums
        binding.spinnerPoiType.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            enumValuesAsList<PoiType>()
        )
        binding.spinnerLitterType.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            enumValuesAsList<LitterType>()
        )
        binding.spinnerBinStatus.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            enumValuesAsList<BinStatus>()
        )
        binding.spinnerBinType.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            enumValuesAsList<BinType>()
        )

        binding.spinnerPoiType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                updateViewForPoiType(PoiType.values()[position])
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        binding.btnAdd.setOnClickListener {
            if (binding.poiTitle.text.toString().isEmpty()) {
                Snackbar.make(binding.root, R.string.enter_poi_title, Snackbar.LENGTH_LONG).show()
            } else {
                var selectedPoiType = PoiType.values()[binding.spinnerPoiType.selectedItemPosition]
                var selectedLitterType: LitterType? = null
                var selectedBinStatus: BinStatus? = null
                var selectedBinType: BinType? = null

                if (selectedPoiType == PoiType.LITTER) {
                    selectedLitterType = LitterType.values()[binding.spinnerLitterType.selectedItemPosition]
                } else if (selectedPoiType == PoiType.BIN) {
                    selectedBinStatus = BinStatus.values()[binding.spinnerBinStatus.selectedItemPosition]
                    selectedBinType = BinType.values()[binding.spinnerBinType.selectedItemPosition]
                }

                presenter.doAddOrSave(
                    title = binding.poiTitle.text.toString(),
                    description = binding.description.text.toString(),
                    poiType = selectedPoiType,
                    isCleanedUp = binding.checkBoxIsCleanedUp.isChecked,
                    litterType = selectedLitterType,
                    binStatus = selectedBinStatus,
                    binType = selectedBinType
                )
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_poi, menu)
        val deleteMenu: MenuItem = menu.findItem(R.id.item_delete)
        deleteMenu.isVisible = presenter.edit
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_delete -> {
                presenter.doDelete()
            }
            R.id.item_cancel -> {
                presenter.doCancel()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun showPoi(poi: PoiModel) {
        binding.poiTitle.setText(poi.title)
        binding.description.setText(poi.description)
        binding.btnAdd.setText(R.string.save_poi)
        Picasso.get()
            .load(poi.image)
            .into(binding.poiImage)
        if (poi.image != Uri.EMPTY) {
            binding.chooseImage.setText(R.string.change_poi_image)
        }

        // Set spinner selections based on the saved values in the POI
        binding.spinnerPoiType.setSelection(poi.poiType.ordinal)

        val currentLitterType = poi.litterType
        val currentBinStatus = poi.binStatus
        val currentBinType = poi.binType

        if (poi.poiType == PoiType.LITTER && currentLitterType != null) {
            binding.spinnerLitterType.setSelection(currentLitterType.ordinal)
            binding.checkBoxIsCleanedUp.isChecked = poi.isCleanedUp
        } else if (poi.poiType == PoiType.BIN) {
            if (currentBinStatus != null) {
                binding.spinnerBinStatus.setSelection(currentBinStatus.ordinal)
            }
            if (currentBinType != null) {
                binding.spinnerBinType.setSelection(currentBinType.ordinal)
            }
        }
    }

    fun updateImage(image: Uri) {
        i("Image updated")
        Picasso.get()
            .load(image)
            .into(binding.poiImage)
        binding.chooseImage.setText(R.string.change_poi_image)
    }

    private fun updateViewForPoiType(poiType: PoiType) {
        when (poiType) {
            PoiType.LITTER -> {
                binding.spinnerLitterType.visibility = View.VISIBLE
                binding.labelLitterType.visibility = View.VISIBLE
                binding.spinnerBinStatus.visibility = View.GONE
                binding.labelBinStatus.visibility = View.GONE
                binding.spinnerBinType.visibility = View.GONE
                binding.labelBinType.visibility = View.GONE
                binding.checkBoxIsCleanedUp.visibility = View.VISIBLE
            }
            PoiType.BIN -> {
                binding.spinnerLitterType.visibility = View.GONE
                binding.labelLitterType.visibility = View.GONE
                binding.spinnerBinStatus.visibility = View.VISIBLE
                binding.labelBinStatus.visibility = View.VISIBLE
                binding.spinnerBinType.visibility = View.VISIBLE
                binding.labelBinType.visibility = View.VISIBLE
                binding.checkBoxIsCleanedUp.visibility = View.GONE
            }
        }
    }

    // Utility function to get names of enum values
    inline fun <reified T : Enum<T>> enumValuesAsList(): List<String> {
        return enumValues<T>().map { it.name }
    }
}