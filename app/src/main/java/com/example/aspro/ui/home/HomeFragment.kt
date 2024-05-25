package com.example.aspro.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.aspro.databinding.FragmentHomeBinding
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateGreetingAndTime()
    }

    fun updateGreetingAndTime() {
        val greetingText: TextView = binding.greetingText
        val currentDateTime: TextView = binding.currentDateTime

        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val greeting = when {
            currentHour < 12 -> "Good Morning"
            currentHour < 18 -> "Good Afternoon"
            else -> "Good Night"
        }

        val currentDateTimeString = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault()).format(Date())

        greetingText.text = greeting
        currentDateTime.text = currentDateTimeString
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}