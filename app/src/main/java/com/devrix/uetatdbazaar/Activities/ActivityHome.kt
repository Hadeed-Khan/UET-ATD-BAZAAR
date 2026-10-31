package com.devrix.uetatdbazaar.Activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.Insets.add
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.devrix.uetatdbazaar.R
import com.devrix.uetatdbazaar.databinding.ActivityHomeBinding

class ActivityHome : AppCompatActivity() {

    // Globals Variables
    lateinit var binding : ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)



        initializeEveryThing()
    }

    // My owns Functions

    fun initializeEveryThing(){


         val tags = mapOf(
            R.id.nav_home to "home",
            R.id.nav_profile to "profile"
        )




    }
}