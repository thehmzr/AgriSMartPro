package com.example.aspro.ui.dashboard.seed

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
import com.example.aspro.databinding.FragmentSeedBinding

class SeedFragment : Fragment() {

    private var _binding: FragmentSeedBinding? = null
    private val binding get() = _binding!!
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var seedAdapter: SeedAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSeedBinding.inflate(inflater, container, false)
        val root: View = binding.root

        databaseHelper = DatabaseHelper(requireContext())

        // Setup RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        seedAdapter = SeedAdapter(databaseHelper.getAllSeeds().toMutableList(), ::onUpdateClicked, ::onDeleteClicked)
        binding.recyclerView.adapter = seedAdapter

        // Add Seed button click listener
        binding.btnAddSeed.setOnClickListener {
            val intent = Intent(activity, SeedFormActivity::class.java)
            startActivity(intent)
        }

        // Load seed data
        loadSeedData()

        return root
    }

    private fun loadSeedData() {
        val seedList = databaseHelper.getAllSeeds()
        seedAdapter.updateData(seedList)
    }

    private fun onUpdateClicked(seed: Seed) {
        val intent = Intent(activity, SeedFormActivity::class.java)
        intent.putExtra("EXTRA_SEED_ID", seed.id)
        startActivity(intent)
    }

    private fun onDeleteClicked(seed: Seed) {
        // Show confirmation dialog
        AlertDialog.Builder(requireContext())
            .setMessage("Are you sure you want to delete this seed?")
            .setPositiveButton("Yes") { dialog, which ->
                databaseHelper.deleteSeed(seed.id)
                Toast.makeText(requireContext(), "Seed deleted successfully", Toast.LENGTH_SHORT).show()
                loadSeedData()
            }
            .setNegativeButton("No", null)
            .show()
    }

    override fun onResume() {
        super.onResume()
        loadSeedData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
