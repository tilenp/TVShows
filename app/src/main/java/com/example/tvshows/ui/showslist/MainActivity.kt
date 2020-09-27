package com.example.tvshows.ui.showslist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tvshows.R
import com.example.tvshows.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.container, ShowsFragment(), SHOWS_FRAGMENT)
                .commit()
        }
    }

    companion object {
        private const val SHOWS_FRAGMENT = "shows_fragment"
    }
}