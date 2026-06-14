package com.example.kfandomhub.ui.fragment

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.kfandomhub.R

class DetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val img = view.findViewById<ImageView>(R.id.imgDetail)
        val name = view.findViewById<TextView>(R.id.tvName)
        val pos = view.findViewById<TextView>(R.id.tvPosition)
        val bioContainer = view.findViewById<LinearLayout>(R.id.bioContainer)

        val memberName = arguments?.getString("name")
        val memberPos = arguments?.getString("position")
        val memberPhotoUrl = arguments?.getString("photoUrl")

        name.text = memberName
        pos.text = memberPos
        Glide.with(this)
            .load(memberPhotoUrl)
            .placeholder(R.drawable.ic_avatar_placeholder)
            .error(R.drawable.ic_avatar_placeholder)
            .centerCrop()
            .into(img)

        renderBiodata(bioContainer)
    }

    private fun renderBiodata(container: LinearLayout) {
        container.removeAllViews()
        val title = TextView(requireContext()).apply {
            text = "Biodata"
            setTextColor(resources.getColor(R.color.primary, null))
            textSize = 18f
            setTypeface(typeface, android.graphics.Typeface.BOLD)
        }
        container.addView(title)

        val rows = listOf(
            "Nama Panggung" to arguments?.getString("stageName"),
            "Nama Lahir" to arguments?.getString("birthName"),
            "Tanggal Lahir" to arguments?.getString("birthday"),
            "Zodiak" to arguments?.getString("zodiacSign"),
            "Zodiak China" to arguments?.getString("chineseZodiacSign"),
            "Tinggi" to arguments?.getString("height"),
            "Berat" to arguments?.getString("weight"),
            "Golongan Darah" to arguments?.getString("bloodType"),
            "MBTI" to arguments?.getString("mbtiType"),
            "Kewarganegaraan" to arguments?.getString("nationality"),
            "Asal/Kelahiran" to arguments?.getString("birthplace"),
            "Instagram" to arguments?.getString("instagram"),
            "Sumber" to arguments?.getString("sourceUrl")
        )

        rows.forEach { (label, value) ->
            if (!value.isNullOrBlank()) {
                container.addView(createBioRow(label, value))
            }
        }
    }

    private fun createBioRow(label: String, value: String): View {
        return LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(0, 12, 0, 0)

            addView(TextView(requireContext()).apply {
                text = label
                setTextColor(resources.getColor(R.color.text_secondary, null))
                textSize = 12f
            })

            addView(TextView(requireContext()).apply {
                text = value
                setTextColor(resources.getColor(R.color.text_primary, null))
                textSize = 14f
                setLineSpacing(2f, 1f)
            })
        }
    }
}
