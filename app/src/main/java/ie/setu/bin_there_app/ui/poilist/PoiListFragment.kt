package ie.setu.bin_there_app.ui.poilist

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ie.setu.bin_there_app.R
import ie.setu.bin_there_app.adapters.PoiAdapter
import ie.setu.bin_there_app.adapters.PoiClickListener
import ie.setu.bin_there_app.databinding.FragmentPoilistBinding
import ie.setu.bin_there_app.main.MainApp
import ie.setu.bin_there_app.models.PoiModel
import ie.setu.bin_there_app.ui.auth.LoggedInViewModel
import ie.setu.bin_there_app.utils.SwipeToDeleteCallback

class PoiListFragment : Fragment(), PoiClickListener {

    lateinit var app: MainApp
    private var _fragBinding: FragmentPoilistBinding? = null
    private val fragBinding get() = _fragBinding!!
    private val poiListViewModel: PoiListViewModel by activityViewModels()
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()

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

        poiListViewModel.observablePoisList.observe(viewLifecycleOwner, Observer {
                pois ->
            pois?.let {
                render(pois as ArrayList<PoiModel>)
                checkSwipeRefresh()
            }
        })

        setSwipeRefresh()

        val swipeDeleteHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = fragBinding.recyclerView.adapter as PoiAdapter
                adapter.removeAt(viewHolder.adapterPosition)
                poiListViewModel.delete(poiListViewModel.liveFirebaseUser.value?.uid!!,
                    (viewHolder.itemView.tag as PoiModel).id!!)
            }
        }
        val itemTouchDeleteHelper = ItemTouchHelper(swipeDeleteHandler)
        itemTouchDeleteHelper.attachToRecyclerView(fragBinding.recyclerView)

        val fab: FloatingActionButton = fragBinding.fab
        fab.setOnClickListener {
            val action = PoiListFragmentDirections.actionPoiListFragmentToAddPoiFragment()
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

    private fun render(poisList: ArrayList<PoiModel>) {
        fragBinding.recyclerView.adapter = PoiAdapter(poisList,this,
            poiListViewModel.readOnly.value!!)
        if (poisList.isEmpty()) {
            fragBinding.recyclerView.visibility = View.GONE
            fragBinding.poisNotFound.visibility = View.VISIBLE
        } else {
            fragBinding.recyclerView.visibility = View.VISIBLE
            fragBinding.poisNotFound.visibility = View.GONE
        }
    }

    override fun onPoiClick(poi: PoiModel) {
        val action = PoiListFragmentDirections.actionPoiListFragmentToPoiDetailFragment(poi.id!!)
        findNavController().navigate(action)
    }

    private fun setSwipeRefresh() {
        fragBinding.swiperefresh.setOnRefreshListener {
            fragBinding.swiperefresh.isRefreshing = true
            if(poiListViewModel.readOnly.value!!)
                poiListViewModel.loadAll()
            else
                poiListViewModel.load()
        }
    }

    private fun checkSwipeRefresh() {
        if (fragBinding.swiperefresh.isRefreshing)
            fragBinding.swiperefresh.isRefreshing = false
    }

    override fun onResume() {
        super.onResume()
        loggedInViewModel.liveFirebaseUser.observe(viewLifecycleOwner, Observer { firebaseUser ->
            if (firebaseUser != null) {
                poiListViewModel.liveFirebaseUser.value = firebaseUser
                poiListViewModel.load()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }
}