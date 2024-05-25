package com.example.aspro.ui.notifications

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Typeface
import android.os.Bundle
import android.provider.MediaStore
import android.text.SpannableString
import android.text.Spanned
import android.text.style.BulletSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aspro.databinding.FragmentNotificationsBinding
import java.util.regex.Pattern

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!
    private lateinit var chatAdapter: ChatAdapter
    private val chatMessages = mutableListOf<ChatMessage>()
    private val SELECT_IMAGE_REQUEST_CODE = 1001
    private val CAPTURE_IMAGE_REQUEST_CODE = 1002

    private var selectedBitmap: Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel = ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        chatAdapter = ChatAdapter(chatMessages)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = chatAdapter

        binding.buttonSubmit.setOnClickListener {
            val userInput = binding.editTextUserInput.text.toString()
            if (userInput.isNotBlank() || selectedBitmap != null) {
                val userMessage = ChatMessage(userInput, true, selectedBitmap)
                chatAdapter.addMessage(userMessage)
                scrollToBottom() // Scroll to the bottom after adding the user message
                binding.editTextUserInput.setText("")  // Clear the EditText after getting the input
                binding.editTextUserInput.isEnabled = false  // Disable EditText
                binding.buttonSubmit.isEnabled = false  // Disable Submit button
                binding.buttonSubmit.visibility = View.INVISIBLE // Hide Submit button
                selectedBitmap = null // Clear selected image after sending
                binding.imagePreviewIcon.visibility = View.GONE // Hide the image preview icon
                binding.watermarkImageView.visibility = View.GONE // Hide the watermark

                if (userMessage.bitmap != null) {
                    notificationsViewModel.generateImageResponse(userInput, userMessage.bitmap) { response ->
                        activity?.runOnUiThread {
                            val responseMessage = ChatMessage(response, false)
                            chatAdapter.addMessage(responseMessage)
                            scrollToBottom() // Scroll to the bottom after adding the response message
                            binding.editTextUserInput.isEnabled = true  // Enable EditText
                            binding.buttonSubmit.isEnabled = true  // Enable Submit button
                            binding.buttonSubmit.visibility = View.VISIBLE // Show Submit button
                        }
                    }
                } else {
                    notificationsViewModel.generateResponse(userInput) { response ->
                        activity?.runOnUiThread {
                            val responseMessage = ChatMessage(response, false)
                            chatAdapter.addMessage(responseMessage)
                            scrollToBottom() // Scroll to the bottom after adding the response message
                            binding.editTextUserInput.isEnabled = true  // Enable EditText
                            binding.buttonSubmit.isEnabled = true  // Enable Submit button
                            binding.buttonSubmit.visibility = View.VISIBLE // Show Submit button
                        }
                    }
                }
            } else {
                Toast.makeText(context, "Please enter a message or select an image", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonSelectImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, SELECT_IMAGE_REQUEST_CODE)
        }

        return root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                SELECT_IMAGE_REQUEST_CODE -> {
                    val imageUri = data?.data
                    val originalBitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, imageUri)
                    val resizedBitmap = getResizedBitmap(originalBitmap, 200, 200) // Adjust size as needed
                    selectedBitmap = resizedBitmap
                    binding.imagePreviewIcon.visibility = View.VISIBLE // Show the image preview icon
                }
                CAPTURE_IMAGE_REQUEST_CODE -> {
                    val originalBitmap = data?.extras?.get("data") as Bitmap
                    val resizedBitmap = getResizedBitmap(originalBitmap, 200, 200) // Adjust size as needed
                    selectedBitmap = resizedBitmap
                    binding.imagePreviewIcon.visibility = View.VISIBLE // Show the image preview icon
                }
            }
        }
    }

    private fun getResizedBitmap(bitmap: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }

    private fun parseTextForFormatting(text: String): SpannableString {
        val pattern = Pattern.compile("(\\*\\s)?\\*\\*(.*?)\\*\\*")
        val matcher = pattern.matcher(text)
        val spannable = SpannableString(text)
        val cleanText = StringBuilder(text)
        var offset = 0

        while (matcher.find()) {
            val start = matcher.start(2) - offset
            val end = matcher.end(2) - offset
            val isBullet = matcher.group(1) != null

            if (isBullet) {
                spannable.setSpan(BulletSpan(16), start - 2, start - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                cleanText.replace(matcher.start(1) - offset, matcher.start(2) - 2 - offset, "")
                offset += 3  // Account for removal of "* **"
            } else {
                cleanText.replace(matcher.start() - offset, matcher.start(2) - 2 - offset, "")
                offset += 4  // Account for removal of "**"
            }

            cleanText.replace(matcher.end(2) - 2 - offset, matcher.end() - offset, "")
            offset += 2  // Account for removal of "**"
            spannable.setSpan(StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        return SpannableString(cleanText.toString())
    }

    private fun scrollToBottom() {
        binding.recyclerView.scrollToPosition(chatAdapter.itemCount - 1)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
