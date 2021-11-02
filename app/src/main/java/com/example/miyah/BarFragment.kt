package com.example.miyah

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.setContentView
import com.example.miyah.databinding.FragmentBarBinding

class BarFragment : Fragment() {

    private lateinit var binding: FragmentBarBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //initialize the data binding variable
        val binding: FragmentBarBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_bar,
            container,
            false)

        return binding.root
    }
}