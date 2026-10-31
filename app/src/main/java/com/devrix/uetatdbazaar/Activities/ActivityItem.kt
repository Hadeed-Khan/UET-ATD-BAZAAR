package com.devrix.uetatdbazaar.Activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.devrix.uetatdbazaar.databinding.ActivityItemBinding

class ActivityItem : AppCompatActivity() {

    // Globals Variables
    lateinit var binding : ActivityItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityItemBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initializeEveryThing()

    }

    // My Owns Functions
    fun initializeEveryThing(){




    }
}