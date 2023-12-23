package ie.setu.bin_there_app.ui.addpoi

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
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
import ie.setu.bin_there_app.ui.auth.LoggedInViewModel
import ie.setu.bin_there_app.ui.poilist.PoiListViewModel

class AddPoiFragment : Fragment() {

    private var _fragBinding: FragmentAddpoiBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val fragBinding get() = _fragBinding!!
    private lateinit var addPoiViewModel: AddPoiViewModel
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()

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

        setButtonListener(fragBinding)
        return root;
    }

    private fun render(status: Boolean) {
        when (status) {
            true -> {
                view?.let {
                    //Uncomment this if you want to immediately return to POI List
                    //findNavController().popBackStack()
                }
            }
            false -> Toast.makeText(context,getString(R.string.poiError),Toast.LENGTH_LONG).show()
        }
    }

    fun setButtonListener(layout: FragmentAddpoiBinding) {
        layout.addPoiButton.setOnClickListener {
            val title = layout.poiTitle.text.toString()
            addPoiViewModel.addPoi(loggedInViewModel.liveFirebaseUser, PoiModel(title = title))
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
}