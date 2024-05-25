package com.example.aspro.ui.dashboard.seed

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aspro.DatabaseHelper
import com.example.aspro.databinding.ActivitySeedFormBinding

class SeedFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySeedFormBinding
    private lateinit var databaseHelper: DatabaseHelper
    private var seedId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeedFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = DatabaseHelper(this)

        // Check if updating an existing seed
        seedId = intent.getIntExtra("EXTRA_SEED_ID", -1)
        if (seedId != -1) {
            loadSeedData(seedId!!)
        }

        binding.btnSave.setOnClickListener { saveSeed() }
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun loadSeedData(seedId: Int) {
        val seed = databaseHelper.getSeedById(seedId)
        seed?.let {
            binding.seedName.setText(it.name)
            binding.seedVariety.setText(it.variety)
            binding.packingWeight.setText(it.packingWeight.toString())
            binding.numberOfPackets.setText(it.numberOfPackets.toString())
            binding.pricePerPacking.setText(it.pricePerPacking.toString())
        }
    }

    private fun saveSeed() {
        val name = binding.seedName.text.toString()
        val variety = binding.seedVariety.text.toString()
        val packingWeight = binding.packingWeight.text.toString().toDoubleOrNull()
        val numberOfPackets = binding.numberOfPackets.text.toString().toIntOrNull()
        val pricePerPacking = binding.pricePerPacking.text.toString().toDoubleOrNull()

        if (name.isNotEmpty() && variety.isNotEmpty() && packingWeight != null && numberOfPackets != null && pricePerPacking != null) {
            val id: Long = if (seedId == -1) {
                databaseHelper.insertSeed(name, variety, packingWeight, numberOfPackets, pricePerPacking)
            } else {
                databaseHelper.updateSeed(seedId!!, name, variety, packingWeight, numberOfPackets, pricePerPacking).toLong()
            }

            if (id > -1) {
                Toast.makeText(this, "Seed added/updated successfully", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Error saving seed", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
        }
    }

}
