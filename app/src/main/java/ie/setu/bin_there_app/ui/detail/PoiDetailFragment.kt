package ie.setu.bin_there_app.ui.detail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ie.setu.bin_there_app.databinding.FragmentPoiDetailBinding
import ie.setu.bin_there_app.ui.auth.LoggedInViewModel
import ie.setu.bin_there_app.ui.poilist.PoiListViewModel
import timber.log.Timber


class PoiDetailFragment : Fragment() {

    private lateinit var detailViewModel: PoiDetailViewModel
    private val args by navArgs<PoiDetailFragmentArgs>()
    private var _fragBinding: FragmentPoiDetailBinding? = null
    private val fragBinding get() = _fragBinding!!
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()
    private val poiListViewModel : PoiListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _fragBinding = FragmentPoiDetailBinding.inflate(inflater, container, false)
        val root = fragBinding.root

        detailViewModel = ViewModelProvider(this).get(PoiDetailViewModel::class.java)
        detailViewModel.observablePoi.observe(viewLifecycleOwner, Observer { render() })

        fragBinding.deletePoiButton.setOnClickListener {
            Timber.i("Deleting POI: ${detailViewModel.observablePoi.value?.id!!}")
            poiListViewModel.delete(loggedInViewModel.liveFirebaseUser.value?.email!!,
                detailViewModel.observablePoi.value?.id!!)
            findNavController().navigateUp()
        }

        fragBinding.cleanupPoiButton.setOnClickListener {
            detailViewModel.cleanupPoi(loggedInViewModel.liveFirebaseUser.value?.uid!!,
                args.poiId, fragBinding.poivm?.observablePoi!!.value!!)
        }

        return root
    }

    private fun render(/*poi: PoiModel*/) {
        fragBinding.poivm = detailViewModel
    }

    override fun onResume() {
        super.onResume()
        detailViewModel.getPoi(loggedInViewModel.liveFirebaseUser.value?.uid!!, args.poiId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }
}