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
import com.example.kfandomhub.util.ProfileManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    @Inject
    lateinit var postDao: PostDao

    private lateinit var imgProfile: ImageView
    private var selectedImageUri: Uri? = null

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data
            if (uri != null) {
                selectedImageUri = uri
                Glide.with(this)
                    .load(selectedImageUri)
                    .placeholder(R.drawable.ic_avatar_placeholder)
                    .error(R.drawable.ic_avatar_placeholder)
                    .circleCrop()
                    .into(imgProfile)
                
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        imgProfile = view.findViewById(R.id.imgProfile)
        val btnChangePicture = view.findViewById<View>(R.id.btnChangePicture)
        val etName = view.findViewById<EditText>(R.id.etProfileName)
        val etUsername = view.findViewById<EditText>(R.id.etProfileUsername)
        val etBio = view.findViewById<EditText>(R.id.etBio)
        val tvPostCount = view.findViewById<TextView>(R.id.tvPostCount)
        val tvBioCount = view.findViewById<TextView>(R.id.tvBioCount)
        val btnSave = view.findViewById<View>(R.id.btnSaveProfile)

        val manager = ProfileManager(requireContext())
        
        etName?.setText(manager.getName())
        etUsername?.setText(manager.getUsername())
        etBio?.setText(manager.getBio())
        tvBioCount.text = "${etBio.text?.length ?: 0}/160"
        etBio.addTextChangedListener { text ->
            tvBioCount.text = "${text?.length ?: 0}/160"
        }

        viewLifecycleOwner.lifecycleScope.launch {
            tvPostCount.text = postDao.getPostCount().toString()
        }
        
        val savedImageUri = manager.getProfileImage()
        if (!savedImageUri.isNullOrEmpty()) {
            try {
                selectedImageUri = Uri.parse(savedImageUri)
                Glide.with(this)
                    .load(selectedImageUri)
                    .placeholder(R.drawable.ic_avatar_placeholder)
                    .error(R.drawable.ic_avatar_placeholder)
                    .circleCrop()
                    .into(imgProfile)
            } catch (e: Exception) {
                e.printStackTrace()
                imgProfile.setImageResource(R.drawable.ic_avatar_placeholder)
            }
        } else {
            Glide.with(this)
                .load("https://api.dicebear.com/9.x/notionists/png?seed=LuvlyBlink")
                .placeholder(R.drawable.ic_avatar_placeholder)
                .error(R.drawable.ic_avatar_placeholder)
                .circleCrop()
                .into(imgProfile)
        }

        btnChangePicture?.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "image/*"
            }
            pickImageLauncher.launch(intent)
        }

        btnSave?.setOnClickListener {
            val name = etName?.text?.toString() ?: ""
            val username = etUsername?.text?.toString() ?: ""
            val bio = etBio?.text?.toString() ?: ""
            
            val displayName = name.ifBlank { "Anonymous" }
            val cleanUsername = username.removePrefix("@").ifBlank { "anonymous" }
            manager.saveProfile(displayName, cleanUsername, bio, selectedImageUri?.toString())
            Toast.makeText(requireContext(), "Profile Saved!", Toast.LENGTH_SHORT).show()
        }
    }
}
