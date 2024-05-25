package com.example.aspro.ui.dashboard.fertilizer

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
import com.example.aspro.databinding.FragmentFertilizerBinding

class FertilizerFragment : Fragment() {

    private var _binding: FragmentFertilizerBinding? = null
    private val binding get() = _binding!!
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var fertilizerAdapter: FertilizerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFertilizerBinding.inflate(inflater, container, false)
        val root: View = binding.root

        databaseHelper = DatabaseHelper(requireContext())

        // Setup RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        fertilizerAdapter = FertilizerAdapter(databaseHelper.getAllFertilizers().toMutableList(), ::onUpdateClicked, ::onDeleteClicked)
        binding.recyclerView.adapter = fertilizerAdapter

        // Add Fertilizer button click listener
        binding.btnAddFertilizer.setOnClickListener {
            val intent = Intent(activity, FertilizerFormActivity::class.java)
            startActivity(intent)
        }

        // Load fertilizer data
        loadFertilizerData()

        return root
    }

    private fun loadFertilizerData() {
        val fertilizerList = databaseHelper.getAllFertilizers()
        fertilizerAdapter.updateData(fertilizerList)
    }

    private fun onUpdateClicked(fertilizer: Fertilizer) {
        val intent = Intent(activity, FertilizerFormActivity::class.java)
        intent.putExtra("EXTRA_FERTILIZER_ID", fertilizer.id)
        startActivity(intent)
    }

    private fun onDeleteClicked(fertilizer: Fertilizer) {
        // Show confirmation dialog
        AlertDialog.Builder(requireContext())
            .setMessage("Are you sure you want to delete this fertilizer?")
            .setPositiveButton("Yes") { dialog, which ->
                databaseHelper.deleteFertilizer(fertilizer.id)
                Toast.makeText(requireContext(), "Fertilizer deleted successfully", Toast.LENGTH_SHORT).show()
                loadFertilizerData()
            }
            .setNegativeButton("No", null)
            .show()
    }

    override fun onResume() {
        super.onResume()
        loadFertilizerData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
