package ie.setu.bin_there_app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ie.setu.bin_there_app.R
import ie.setu.bin_there_app.databinding.CardPoiBinding
import ie.setu.bin_there_app.models.PoiModel

interface PoiClickListener {
    fun onPoiClick(poi: PoiModel)
}

class PoiAdapter constructor(private var pois: List<PoiModel>,
                             private val listener: PoiClickListener)
    : RecyclerView.Adapter<PoiAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardPoiBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val poi = pois[holder.adapterPosition]
        holder.bind(poi,listener)
    }

    override fun getItemCount(): Int = pois.size

    inner class MainHolder(val binding : CardPoiBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(poi: PoiModel, listener: PoiClickListener) {
            binding.poi = poi
            Picasso.get().load(poi.image.toUri())
                .resize(200, 200)
                .centerCrop()
                .into(binding.imageIcon)
            binding.root.setOnClickListener { listener.onPoiClick(poi) }
            binding.executePendingBindings()
        }
    }
}