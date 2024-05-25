package com.example.aspro.ui.dashboard.seed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aspro.databinding.ItemSeedBinding

class SeedAdapter(
    private var seeds: MutableList<Seed>,
    private val onUpdateClicked: (Seed) -> Unit,
    private val onDeleteClicked: (Seed) -> Unit
) : RecyclerView.Adapter<SeedAdapter.SeedViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeedViewHolder {
        val binding = ItemSeedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SeedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SeedViewHolder, position: Int) {
        val seed = seeds[position]
        holder.bind(seed)
        holder.binding.updateButton.setOnClickListener { onUpdateClicked(seed) }
        holder.binding.deleteButton.setOnClickListener { onDeleteClicked(seed) }
    }

    override fun getItemCount(): Int {
        return seeds.size
    }

    fun updateData(newSeeds: List<Seed>) {
        seeds.clear()
        seeds.addAll(newSeeds)
        notifyDataSetChanged()
    }

    inner class SeedViewHolder(val binding: ItemSeedBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(seed: Seed) {
            binding.seedName.text = seed.name
            binding.variety.text = "Variety: ${seed.variety}"
            binding.packingWeight.text = "Weight: ${seed.packingWeight} Kgs"
            binding.numberOfPackets.text = "Packet: ${seed.numberOfPackets}"
            binding.pricePerPacking.text = "Price: $ ${seed.pricePerPacking}"
        }
    }
}
