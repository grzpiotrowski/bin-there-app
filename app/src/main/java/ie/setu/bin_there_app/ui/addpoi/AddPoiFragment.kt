package ie.setu.bin_there_app.ui.addpoi

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import ie.setu.bin_there_app.R
import ie.setu.bin_there_app.databinding.FragmentAddpoiBinding
import ie.setu.bin_there_app.models.PoiModel
import ie.setu.bin_there_app.models.PoiType
import ie.setu.bin_there_app.models.LitterType
import ie.setu.bin_there_app.models.BinStatus
import ie.setu.bin_there_app.models.BinType
import ie.setu.bin_there_app.ui.auth.LoggedInViewModel
import ie.setu.bin_there_app.ui.poilist.PoiListViewModel
import ie.setu.bin_there_app.firebase.FirebaseImageManager

class AddPoiFragment : Fragment() {

    private var _fragBinding: FragmentAddpoiBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val fragBinding get() = _fragBinding!!
    private lateinit var addPoiViewModel: AddPoiViewModel
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()
    private var poiImageUri: Uri? = null
    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                poiImageUri = uri
                addPoiViewModel.onImageSelected(uri)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _fragBinding = FragmentAddpoiBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        // activity?.title = getString(R.string.action_addPoi)
        setupMenu()
        addPoiViewModel = ViewModelProvider(this).get(AddPoiViewModel::class.java)
        addPoiViewModel.observableStatus.observe(viewLifecycleOwner, Observer {
                status -> status?.let { render(status) }
        })

        fragBinding.uploadPhotoButton.setOnClickListener {
            val chooseFile = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                type = "image/*"
                addCategory(Intent.CATEGORY_OPENABLE)
            }
            val intent = Intent.createChooser(chooseFile, getString(R.string.select_image))
            imagePickerLauncher.launch(intent)
        }
        setupSpinners(fragBinding)
        setButtonListener(fragBinding)
        return root;
    }

    private fun render(status: Boolean) {
        when (status) {
            true -> {
                view?.let {
                    //Uncomment this if you want to immediately return to POI List
                    findNavController().popBackStack()
                }
            }
            false -> Toast.makeText(context,getString(R.string.poiError),Toast.LENGTH_LONG).show()
        }
    }

    fun setButtonListener(layout: FragmentAddpoiBinding) {
        layout.addPoiButton.setOnClickListener {
            val title = layout.poiTitle.text.toString()
            val description = layout.description.text.toString()
            val isCleanedUp = layout.checkBoxIsCleanedUp.isChecked
            val poiType = PoiType.values()[layout.spinnerPoiType.selectedItemPosition]
            var litterType: LitterType? = null
            var binStatus: BinStatus? = null
            var binType: BinType? = null

            if (poiType == PoiType.LITTER) {
                litterType = LitterType.values()[layout.spinnerLitterType.selectedItemPosition]
            } else if (poiType == PoiType.BIN) {
                binStatus = BinStatus.values()[layout.spinnerBinStatus.selectedItemPosition]
                binType = BinType.values()[layout.spinnerBinType.selectedItemPosition]
            }
            addPoiViewModel.addPoi(loggedInViewModel.liveFirebaseUser, PoiModel(title = title,
                    description = description,
                    poiType = poiType,
                    dateReported = System.currentTimeMillis(),
                    isCleanedUp = isCleanedUp,
                    litterType = litterType,
                    binStatus = binStatus,
                    binType = binType), poiImageUri)
        }
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                // Handle for example visibility of menu items
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_addpoi, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Validate and handle the selected menu item
                return NavigationUI.onNavDestinationSelected(menuItem,
                    requireView().findNavController())
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setupSpinners(binding: FragmentAddpoiBinding) {
        // Set up the spinners with the enums
        binding.spinnerPoiType.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            enumValuesAsList<PoiType>()
        )
        binding.spinnerLitterType.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            enumValuesAsList<LitterType>()
        )
        binding.spinnerBinStatus.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            enumValuesAsList<BinStatus>()
        )
        binding.spinnerBinType.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            enumValuesAsList<BinType>()
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    override fun onResume() {
        super.onResume()
        val poiListViewModel = ViewModelProvider(this).get(PoiListViewModel::class.java)
        poiListViewModel.observablePoisList.observe(viewLifecycleOwner, Observer {
        })
    }

    inline fun <reified T : Enum<T>> enumValuesAsList(): List<String> {
        return enumValues<T>().map { it.name }
    }
}