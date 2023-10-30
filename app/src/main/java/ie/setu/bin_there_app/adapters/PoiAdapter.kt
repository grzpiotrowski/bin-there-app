package ie.setu.bin_there_app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ie.setu.bin_there_app.databinding.CardPoiBinding
import ie.setu.bin_there_app.models.PoiModel

interface PoiListener {
    fun onPoiClick(poi: PoiModel, position: Int)
}

class PoiAdapter constructor(private var pois: List<PoiModel>,
                            private val listener: PoiListener) :
    RecyclerView.Adapter<PoiAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardPoiBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val poi = pois[holder.adapterPosition]
        holder.bind(poi, listener)
    }

    override fun getItemCount(): Int = pois.size

    class MainHolder(private val binding : CardPoiBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(poi: PoiModel, listener: PoiListener) {
            binding.poiTitle.text = poi.title
            binding.description.text = poi.description
            Picasso.get().load(poi.image).resize(200,200).into(binding.imageIcon)
            binding.root.setOnClickListener { listener.onPoiClick(poi, adapterPosition) }
        }
    }
}