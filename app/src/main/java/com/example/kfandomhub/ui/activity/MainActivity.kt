package com.example.kfandomhub.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.kfandomhub.R
import com.example.kfandomhub.ui.fragment.AddFragment
import com.example.kfandomhub.ui.fragment.FeedFragment
import com.example.kfandomhub.ui.fragment.HomeFragment
import com.example.kfandomhub.ui.fragment.ProfileFragment
import com.example.kfandomhub.ui.fragment.TrendingFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val lifecycleTag = "KFandomHubLifecycle"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(lifecycleTag, "onCreate: Activity dibuat")
        setContentView(R.layout.activity_main)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
        }

        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> replaceFragment(HomeFragment())
                R.id.trending -> replaceFragment(TrendingFragment())
                R.id.feed -> replaceFragment(FeedFragment())
                R.id.add -> replaceFragment(AddFragment())
                R.id.profile -> replaceFragment(ProfileFragment())
            }
            true
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(lifecycleTag, "onStart: Activity mulai terlihat")
    }

    override fun onResume() {
        super.onResume()
        Log.d(lifecycleTag, "onResume: Activity siap digunakan")
    }

    override fun onPause() {
        super.onPause()
        Log.d(lifecycleTag, "onPause: Activity kehilangan fokus")
    }

    override fun onStop() {
        super.onStop()
        Log.d(lifecycleTag, "onStop: Activity tidak terlihat")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(lifecycleTag, "onRestart: Activity dibuka kembali")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(lifecycleTag, "onDestroy: Activity dihancurkan")
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .setReorderingAllowed(true)
            .commit()
    }
}
