package com.devrix.uetatdbazaar.Activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.devrix.uetatdbazaar.R
import com.devrix.uetatdbazaar.databinding.ActivityLoginBinding
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth


class ActivityLogin : AppCompatActivity() {

    // Globals Variables
    lateinit var binding: ActivityLoginBinding
    lateinit var analytics: FirebaseAnalytics
    lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initializeEveryThing()



    }
    // My owns Functions

    fun initializeEveryThing(){

        analytics = FirebaseAnalytics.getInstance(this@ActivityLogin)
        auth = FirebaseAuth.getInstance()

        // for Move the Registration Activity
        binding.btnCreateAccount.setOnClickListener {
            startActivity(Intent(this@ActivityLogin, ActivityRegister::class.java))
            finish()
        }

    }
}