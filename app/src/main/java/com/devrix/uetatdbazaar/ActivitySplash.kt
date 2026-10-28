package com.devrix.uetatdbazaar

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.devrix.uetatdbazaar.databinding.ActivitySplashBinding

class ActivitySplash : AppCompatActivity() {

    lateinit var binding : ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)



        initializeEveryThing()

    }

    // My Own functions
    fun initializeEveryThing()
    {




    }

}