package ie.setu.bin_there_app.ui.poilist

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ie.setu.bin_there_app.R
import ie.setu.bin_there_app.adapters.PoiAdapter
import ie.setu.bin_there_app.adapters.PoiClickListener
import ie.setu.bin_there_app.databinding.FragmentPoilistBinding
import ie.setu.bin_there_app.main.MainApp
import ie.setu.bin_there_app.models.PoiModel

class PoiListFragment : Fragment(), PoiClickListener {

    lateinit var app: MainApp
    private var _fragBinding: FragmentPoilistBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var poiListViewModel: PoiListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentPoilistBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        setupMenu()
        fragBinding.recyclerView.layoutManager = LinearLayoutManager(activity)
        poiListViewModel = ViewModelProvider(this).get(PoiListViewModel::class.java)
        poiListViewModel.observablePoisList.observe(viewLifecycleOwner, Observer {
                donations ->
            donations?.let { render(donations) }
        })

        val fab: FloatingActionButton = fragBinding.fab
        fab.setOnClickListener {
            val action = PoiListFragmentDirections.actionReportFragmentToDonateFragment()
            findNavController().navigate(action)
        }
        return root
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_poilist, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Validate and handle the selected menu item
                return NavigationUI.onNavDestinationSelected(menuItem,
                    requireView().findNavController())
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun render(donationsList: List<PoiModel>) {
        fragBinding.recyclerView.adapter = PoiAdapter(donationsList,this)
        if (donationsList.isEmpty()) {
            fragBinding.recyclerView.visibility = View.GONE
            fragBinding.poisNotFound.visibility = View.VISIBLE
        } else {
            fragBinding.recyclerView.visibility = View.VISIBLE
            fragBinding.poisNotFound.visibility = View.GONE
        }
    }

    override fun onPoiClick(poi: PoiModel) {
        val action = PoiListFragmentDirections.actionPoiListFragmentToPoiDetailFragment(poi.id)
        findNavController().navigate(action)
    }

    override fun onResume() {
        super.onResume()
        poiListViewModel.load()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }
}