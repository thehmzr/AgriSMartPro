package com.example.aspro.ui.dashboard.pesticide

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aspro.DatabaseHelper
import com.example.aspro.databinding.FragmentPesticideBinding

class PesticideFragment : Fragment() {

    private var _binding: FragmentPesticideBinding? = null
    private val binding get() = _binding!!
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var pesticideAdapter: PesticideAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPesticideBinding.inflate(inflater, container, false)
        val root: View = binding.root

        databaseHelper = DatabaseHelper(requireContext())

        // Setup RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        pesticideAdapter = PesticideAdapter(databaseHelper.getAllPesticides().toMutableList(), ::onUpdateClicked, ::onDeleteClicked)
        binding.recyclerView.adapter = pesticideAdapter

        // Add Pesticide button click listener
        binding.btnAddPesticide.setOnClickListener {
            val intent = Intent(activity, PesticideFormActivity::class.java)
            startActivity(intent)
        }

        // Load pesticide data
        loadPesticideData()

        return root
    }

    private fun loadPesticideData() {
        val pesticideList = databaseHelper.getAllPesticides()
        pesticideAdapter.updateData(pesticideList)
    }

    private fun onUpdateClicked(pesticide: Pesticide) {
        val intent = Intent(activity, PesticideFormActivity::class.java)
        intent.putExtra("EXTRA_PESTICIDE_ID", pesticide.id)
        startActivity(intent)
    }

    private fun onDeleteClicked(pesticide: Pesticide) {
        // Show confirmation dialog
        AlertDialog.Builder(requireContext())
            .setMessage("Are you sure you want to delete this pesticide?")
            .setPositiveButton("Yes") { dialog, which ->
                databaseHelper.deletePesticide(pesticide.id)
                Toast.makeText(requireContext(), "Pesticide deleted successfully", Toast.LENGTH_SHORT).show()
                loadPesticideData()
            }
            .setNegativeButton("No", null)
            .show()
    }

    override fun onResume() {
        super.onResume()
        loadPesticideData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
