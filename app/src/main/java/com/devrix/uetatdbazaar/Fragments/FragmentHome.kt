package com.devrix.uetatdbazaar.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.devrix.uetatdbazaar.R
import com.devrix.uetatdbazaar.databinding.FragmentHomeBinding


class FragmentHome : Fragment() {
    // Globals Variables
    lateinit var binding : FragmentHomeBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentHomeBinding.inflate(layoutInflater)



        return binding.root
    }


}