package ie.setu.bin_there_app.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ie.setu.bin_there_app.databinding.ActivityPoiListBinding
import ie.setu.bin_there_app.databinding.CardPoiBinding
import ie.setu.bin_there_app.main.MainApp
import ie.setu.bin_there_app.models.PoiModel

class PoiListActivity : AppCompatActivity() {

    lateinit var app: MainApp
    private lateinit var binding: ActivityPoiListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPoiListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = PoiAdapter(app.pois)
    }
}

class PoiAdapter constructor(private var pois: List<PoiModel>) :
        RecyclerView.Adapter<PoiAdapter.MainHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardPoiBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }
    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val poi = pois[holder.adapterPosition]
        holder.bind(poi)
    }

    override fun getItemCount(): Int = pois.size

    class MainHolder(private val binding : CardPoiBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(poi: PoiModel) {
            binding.poiTitle.text = poi.title
            binding.description.text = poi.description
        }
    }
}