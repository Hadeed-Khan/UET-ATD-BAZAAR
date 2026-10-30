package com.devrix.uetatdbazaar.Activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.devrix.uetatdbazaar.ApplicationClass.NetworkClass
import com.devrix.uetatdbazaar.R
import com.devrix.uetatdbazaar.databinding.ActivityLoginBinding
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException


class ActivityLogin : AppCompatActivity() {

    // Globals Variables
    lateinit var binding: ActivityLoginBinding
    lateinit var networkClass : NetworkClass
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

        networkClass = NetworkClass(this@ActivityLogin)
        analytics = FirebaseAnalytics.getInstance(this@ActivityLogin)
        auth = FirebaseAuth.getInstance()
        var currentUser = auth.currentUser

        // Check If User Already Login Then Show Main Screen
        if (currentUser != null)
        {
            startActivity(Intent(this@ActivityLogin, ActivityHome::class.java))
            finish()
        }

        // for Move the Registration Activity
        binding.btnCreateAccount.setOnClickListener {
            startActivity(Intent(this@ActivityLogin, ActivityRegister::class.java))
            finish()
        }

        binding.btnLogin.setOnClickListener {
            loginAccount()
        }

    }

    fun loginAccount(){

        var email = binding.etEmail.text.toString()
        var password = binding.etPassword.text.toString()

        // For Check Network is On
        if (!(networkClass.isOnline())){
            Toast.makeText(this@ActivityLogin, "No Internet Connection", Toast.LENGTH_SHORT).show()
            return
        }

        // Input Validation
        if (binding.etEmail.text?.isEmpty() == true || binding.etPassword.text?.isEmpty() == true)
        {
            Toast.makeText(this@ActivityLogin, "Please Enter Data", Toast.LENGTH_SHORT).show()
            return
        }

        // Check the email is Correct Enter or not
        if (!(binding.etEmail.text?.contains("@gmail.com") == true)){
            binding.tilEmail.error = "Please Correct Email"
            return
        }

        // For Login the user
        setLoading(true)
        auth.signInWithEmailAndPassword(email,password).addOnSuccessListener {
            Toast.makeText(this@ActivityLogin, "Account Login", Toast.LENGTH_SHORT).show()
            setLoading(false)
            startActivity(Intent(this@ActivityLogin, ActivityHome::class.java))
            finish()
        }.addOnFailureListener { e ->
            setLoading(false)
            when(e)
            {
                is FirebaseAuthInvalidUserException ->
                    Toast.makeText(this@ActivityLogin, "No account found with this email. Please create an account.", Toast.LENGTH_SHORT).show()
                is FirebaseAuthInvalidCredentialsException ->
                    Toast.makeText(this@ActivityLogin, "Email or password is incorrect.", Toast.LENGTH_SHORT).show()
                is FirebaseNetworkException ->
                    Toast.makeText(this@ActivityLogin, "No internet connection. Please try again.", Toast.LENGTH_SHORT).show()
                else ->
                    Toast.makeText(this@ActivityLogin, "$e error", Toast.LENGTH_SHORT).show()
            }
        }

    }

        fun setLoading(loading: Boolean) {
        binding.loader.visibility = if (loading) View.VISIBLE else View.GONE
        binding.btnLogin.text = if (loading) "" else "Log in"
        binding.btnLogin.isEnabled = !loading
        binding.btnCreateAccount.isEnabled = !loading
        binding.tvForgot.isEnabled = !loading
    }
}