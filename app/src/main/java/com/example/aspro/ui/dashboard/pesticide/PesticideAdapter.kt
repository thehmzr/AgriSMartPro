package com.example.aspro.ui.dashboard.pesticide

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aspro.databinding.ItemPesticideBinding

class PesticideAdapter(
    private var pesticides: MutableList<Pesticide>,
    private val onUpdateClicked: (Pesticide) -> Unit,
    private val onDeleteClicked: (Pesticide) -> Unit
) : RecyclerView.Adapter<PesticideAdapter.PesticideViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PesticideViewHolder {
        val binding = ItemPesticideBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PesticideViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PesticideViewHolder, position: Int) {
        val pesticide = pesticides[position]
        holder.bind(pesticide)
        holder.binding.updateButton.setOnClickListener { onUpdateClicked(pesticide) }
        holder.binding.deleteButton.setOnClickListener { onDeleteClicked(pesticide) }
    }

    override fun getItemCount(): Int {
        return pesticides.size
    }

    fun updateData(newPesticides: List<Pesticide>) {
        pesticides.clear()
        pesticides.addAll(newPesticides)
        notifyDataSetChanged()
    }

    inner class PesticideViewHolder(val binding: ItemPesticideBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(pesticide: Pesticide) {
            binding.pesticideName.text = pesticide.name
            binding.company.text = "Company: ${pesticide.company}"
            binding.literKilogram.text = "Liters/Kilograms: ${pesticide.literKilogram}"
            binding.numberOfPackets.text = "Packets: ${pesticide.numberOfPackets}"
            binding.pricePerPacking.text = "Price: $${pesticide.pricePerPacking}"
        }
    }
}
