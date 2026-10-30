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
import com.devrix.uetatdbazaar.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException

class ActivityRegister : AppCompatActivity() {

    // Globals Variables
    lateinit var binding: ActivityRegisterBinding
    lateinit var networkClass: NetworkClass
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initializeEveryThing()
    }

    // My Owns Functions
    fun initializeEveryThing() {

        networkClass = NetworkClass(this@ActivityRegister)
        auth = FirebaseAuth.getInstance()
        registerStudent()


        // For the move the login screen
        binding.tvLogin.setOnClickListener {
            var intent = Intent(this@ActivityRegister, ActivityLogin::class.java)
            startActivity(intent)
            finish()
        }

    }

 // This function is for the creating the account
    fun registerStudent(){

        binding.btnRegister.setOnClickListener {

            // For Checking The Network is On aur Off
            if (networkClass.isOnline()) {
                if (inputValidation()) {

                    var email = binding.etEmail.text.toString().trim()
                    var password = binding.etPassword.text.toString().trim()

                    binding.progress.visibility = View.VISIBLE
                    binding.btnRegister.isEnabled = false
                    auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task ->
                        binding.progress.visibility = View.GONE

                        clearInput()
                        if (task.isSuccessful){
                            Toast.makeText(this@ActivityRegister, "Account Created..", Toast.LENGTH_SHORT).show()
                            binding.btnRegister.isEnabled = true
                            auth.signOut()
                            startActivity(Intent(this@ActivityRegister, ActivityLogin::class.java))
                            finish()
                        }


                    }.addOnFailureListener { e ->
                        when(e)
                        {
                            is FirebaseAuthUserCollisionException -> {
                                binding.emailLayout.error = "This email is already registered. Please log in."
                            }
                        }
                        binding.progress.visibility = View.GONE
                        clearInput()
                        binding.btnRegister.isEnabled = true
                    }


                }
            }
            else{
                Toast.makeText(this@ActivityRegister, "No Internet Connection...", Toast.LENGTH_SHORT).show()
            }
        }

    }

    // Check All Fields are Fill
    fun inputValidation() : Boolean{

        if (binding.etName.text?.isEmpty() == true || binding.deptSP.selectedItemPosition == 0 || binding.semesterSP.selectedItemPosition == 0 || binding.etPhone.text?.isEmpty() == true || binding.etEmail.text?.isEmpty() == true || binding.etPassword.text?.isEmpty() == true){
            Toast.makeText(this@ActivityRegister, "Please Fill All Data", Toast.LENGTH_SHORT).show()
            return false
        }

        // check for Phone
        if (binding.etPhone.length() != 11)
        {
            binding.phoneLayout.error = "Please Enter Correct Number"
            return false
        }

        // check For Email
        if(binding.etEmail.text?.contains("@gmail.com") != true){
            binding.emailLayout.error = "Please Enter Correct Email..."
            return false
        }

        // Check The Password Length
        if (binding.etPassword.length()<6)
        {
            binding.passwordLayout.error = "Choose at least 6 characters Password"
        return false
        }



    return true
    }

    // For Clear All Fields
    fun clearInput(){
        binding.etName.text?.clear()
        binding.deptSP.setSelection(0)
        binding.semesterSP.setSelection(0)
        binding.etPhone.text?.clear()
        binding.etEmail.text?.clear()
        binding.etPassword.text?.clear()

        binding.nameLayout.error = null
        binding.phoneLayout.error = null
        binding.emailLayout.error = null
        binding.passwordLayout.error = null
    }

}