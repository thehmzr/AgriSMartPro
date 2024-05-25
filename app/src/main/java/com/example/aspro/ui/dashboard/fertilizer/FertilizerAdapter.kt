package com.example.aspro.ui.dashboard.fertilizer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aspro.databinding.ItemFertilizerBinding

class FertilizerAdapter(
    private var fertilizers: MutableList<Fertilizer>,
    private val onUpdateClicked: (Fertilizer) -> Unit,
    private val onDeleteClicked: (Fertilizer) -> Unit
) : RecyclerView.Adapter<FertilizerAdapter.FertilizerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FertilizerViewHolder {
        val binding = ItemFertilizerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FertilizerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FertilizerViewHolder, position: Int) {
        val fertilizer = fertilizers[position]
        holder.bind(fertilizer)
        holder.binding.updateButton.setOnClickListener { onUpdateClicked(fertilizer) }
        holder.binding.deleteButton.setOnClickListener { onDeleteClicked(fertilizer) }
    }

    override fun getItemCount(): Int {
        return fertilizers.size
    }

    fun updateData(newFertilizers: List<Fertilizer>) {
        fertilizers.clear()
        fertilizers.addAll(newFertilizers)
        notifyDataSetChanged()
    }

    inner class FertilizerViewHolder(val binding: ItemFertilizerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(fertilizer: Fertilizer) {
            binding.fertilizerName.text = fertilizer.name
            binding.company.text = "Variety: ${fertilizer.company}"
            binding.packingWeight.text = "Weight: ${fertilizer.packingWeight} Kgs"
            binding.numberOfPackets.text = "Packet: ${fertilizer.numberOfPackets}"
            binding.pricePerPacking.text = "Price: $ ${fertilizer.pricePerPacking}"
        }
    }
}
