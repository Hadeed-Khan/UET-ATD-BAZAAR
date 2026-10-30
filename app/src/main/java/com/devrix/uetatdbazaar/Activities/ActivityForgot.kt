package com.devrix.uetatdbazaar.Activities

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.devrix.uetatdbazaar.ApplicationClass.NetworkClass
import com.devrix.uetatdbazaar.R
import com.devrix.uetatdbazaar.databinding.ActivityForgotBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import kotlin.concurrent.timer

class ActivityForgot : AppCompatActivity() {

    // Globals Variables
    lateinit var binding: ActivityForgotBinding
    lateinit var networkClass: NetworkClass
    lateinit var auth: FirebaseAuth
    private var timer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityForgotBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initializeEveryThing()

    }
    // My owns functions

    fun initializeEveryThing() {

        auth = FirebaseAuth.getInstance()

        networkClass = NetworkClass(this@ActivityForgot)

        // for back the screen of Login
        binding.tvBack.setOnClickListener {
            startActivity(Intent(this@ActivityForgot, ActivityLogin::class.java))
            finish()
        }

        // For Send the reset link to the user email
        binding.btnReset.setOnClickListener {
            sendResetLink()
        }
    }

    fun sendResetLink() {
        var email = binding.etEmail.text.toString()

        // check the internet is On
        if (!(networkClass.isOnline())) {
            Toast.makeText(this@ActivityForgot, "No Internet Connection..", Toast.LENGTH_SHORT)
                .show()
            return
        }

        // Check the email is fields
        if (binding.etEmail.text?.isEmpty() == true) {
            binding.tilEmail.error = "Please Enter The Email"
            return
        }

        // Check Email is Correct
        if (!(email.contains("@gmail.com"))) {
            binding.tilEmail.error = "Please Enter Correct Email"
            return
        }

        binding.btnReset.isEnabled = false

        setLoading(true)
        binding.successCard.visibility = View.GONE
        auth.sendPasswordResetEmail(email).addOnSuccessListener {
            Toast.makeText(this@ActivityForgot, "Reset Link Sent", Toast.LENGTH_SHORT).show()
            setLoading(false)
            binding.tvSuccessMsg.text =
                "If an account exists for $email, you will receive a reset link shortly. " +
                        "Please check your spam folder too."
            binding.successCard.visibility = View.VISIBLE
            binding.btnReset.isEnabled = true
            binding.etEmail.text?.clear()
            binding.tilEmail.error = null

            startCooldown()

        }.addOnFailureListener { e ->
            setLoading(false)
            val message = when (e) {
                is FirebaseNetworkException -> "No internet connection. Please try again."
                is FirebaseTooManyRequestsException -> "Too many attempts. Try again later."
                else -> "Could not send reset email. Please try again."
            }
            Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
            binding.btnReset.isEnabled = true

            binding.etEmail.text?.clear()
            binding.tilEmail.error = null
        }


    }

    fun setLoading(loading: Boolean) {
        binding.loader.visibility = if (loading) View.VISIBLE else View.GONE
        if (loading) binding.btnReset.text = ""
        else if (timer == null) binding.btnReset.text = "Reset password"
        binding.btnReset.isEnabled = !loading
        binding.tvBack.isEnabled = !loading
    }
    fun startCooldown() {
        binding.btnReset.isEnabled = false
        timer?.cancel()
        timer = object : CountDownTimer(30_000, 1_000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.btnReset.text = "Resend in ${millisUntilFinished / 1000}s"
            }
            override fun onFinish() {
                binding.btnReset.text = "Resend link"
                binding.btnReset.isEnabled = true
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }
}