package com.example.miyah

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.setContentView
import com.example.miyah.databinding.FragmentBarBinding

class BarFragment : Fragment() {

    private lateinit  var binding: FragmentBarBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentBarBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_bar,
            container,
            false)

        updateProgressBar()


        binding.buttonIncr.setOnClickListener {
            if (progr <= 90) {
                progr += 10
                updateProgressBar()
            }
        }

        binding.buttonDecr.setOnClickListener {
            if (progr >= 10) {
                progr -= 10
                updateProgressBar()
            }
        }
        // Inflate the layout for this fragment
        return binding.root
    }

    private var progr = 0

    private fun updateProgressBar() {
        binding.progressBar.progress = progr
        binding.textViewProgress.text = "$progr%"
    }
}