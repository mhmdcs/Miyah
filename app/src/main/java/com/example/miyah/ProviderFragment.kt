package com.example.miyah

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.miyah.databinding.FragmentProviderBinding
import com.example.miyah.databinding.FragmentSignupBinding

class ProviderFragment : Fragment() {

    private lateinit var binding: FragmentProviderBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //initialize the data binding variable
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_provider,
            container,
            false)

        return binding.root
    }

}