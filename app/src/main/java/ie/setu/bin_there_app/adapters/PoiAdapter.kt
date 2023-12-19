package ie.setu.bin_there_app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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
            binding.imageIcon.setImageResource(R.mipmap.ic_launcher_round)
            binding.root.setOnClickListener { listener.onPoiClick(poi) }
            binding.executePendingBindings()
        }
    }
}