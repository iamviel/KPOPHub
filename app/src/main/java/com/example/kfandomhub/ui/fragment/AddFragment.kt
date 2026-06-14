package com.example.kfandomhub.ui.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.kfandomhub.R
import com.example.kfandomhub.data.database.PostDao
import com.example.kfandomhub.data.model.Post
import com.example.kfandomhub.util.ProfileManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AddFragment : Fragment() {

    @Inject
    lateinit var postDao: PostDao

    private lateinit var img: ImageView
    private lateinit var uploadPlaceholder: View
    private var imageUri: Uri? = null

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            imageUri = result.data?.data
            Glide.with(this)
                .load(imageUri)
                .placeholder(R.drawable.ic_image_placeholder)
                .error(R.drawable.ic_image_placeholder)
                .centerCrop()
                .into(img)
            uploadPlaceholder.visibility = View.GONE
            
            imageUri?.let { uri ->
                try {
                    val contentResolver = requireContext().contentResolver
                    val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION
                    contentResolver.takePersistableUriPermission(uri, takeFlags)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        img = view.findViewById(R.id.imgPreview)
        uploadPlaceholder = view.findViewById(R.id.uploadPlaceholder)
        val btnPick = view.findViewById<View>(R.id.btnPick)
        val btnPost = view.findViewById<View>(R.id.btnPost)
        val etTitle = view.findViewById<EditText>(R.id.etTitle)
        val etCaption = view.findViewById<EditText>(R.id.etCaption)
        val etTags = view.findViewById<EditText>(R.id.etTags)
        val tvTitleCount = view.findViewById<TextView>(R.id.tvTitleCount)
        val tvCaptionCount = view.findViewById<TextView>(R.id.tvCaptionCount)

        etTitle.addTextChangedListener { text ->
            tvTitleCount.text = "${text?.length ?: 0}/50"
        }
        etCaption.addTextChangedListener { text ->
            tvCaptionCount.text = "${text?.length ?: 0}/500"
        }

        btnPick.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "image/*"
            }
            pickImageLauncher.launch(intent)
        }

        btnPost.setOnClickListener {
            val currentUri = imageUri
            val title = etTitle.text.toString()
            val caption = etCaption.text.toString()
            val tags = normalizeTags(etTags.text.toString())
            
            val manager = ProfileManager(requireContext())
            val authorName = manager.getName()
            val authorUsername = manager.getUsername()
            val authorImageUri = manager.getProfileImage()

            if (currentUri != null && title.isNotBlank()) {
                val post = Post(
                    imageUri = currentUri.toString(),
                    title = title,
                    caption = caption,
                    authorName = authorName,
                    authorUsername = authorUsername,
                    authorImageUri = authorImageUri,
                    tags = tags
                )
                
                viewLifecycleOwner.lifecycleScope.launch {
                    try {
                        postDao.insertPost(post)
                        
                        Toast.makeText(requireContext(), "Posted!", Toast.LENGTH_SHORT).show()
                        img.setImageDrawable(null)
                        uploadPlaceholder.visibility = View.VISIBLE
                        etTitle.text.clear()
                        etCaption.text.clear()
                        etTags.text.clear()
                        imageUri = null
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            } else if (currentUri == null) {
                Toast.makeText(requireContext(), "Please pick an image first", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Please enter a title", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun normalizeTags(rawTags: String): String {
        return rawTags
            .split(" ", ",")
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .joinToString(" ") { tag ->
                if (tag.startsWith("#")) tag else "#$tag"
            }
    }
}
