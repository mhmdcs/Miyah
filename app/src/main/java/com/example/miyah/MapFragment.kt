package com.example.miyah

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.example.miyah.databinding.FragmentMapBinding
import com.example.miyah.databinding.FragmentProviderBinding
import com.example.miyah.databinding.FragmentSignupBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment

class MapFragment : Fragment() {

    private lateinit var binding: FragmentMapBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //initialize the data binding variable
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_map,
            container,
            false)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        binding.getLocationButton.setOnClickListener {
            fetchLocation()
        }


        return binding.root
    }

    private fun fetchLocation() {
        val task = fusedLocationProviderClient.lastLocation

        if(ActivityCompat.checkSelfPermission(requireContext(),Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(requireContext(),Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED){
          ActivityCompat.requestPermissions(requireActivity(),arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 101)
            return
        }
        task.addOnSuccessListener {
            if(it != null){
                Toast.makeText(activity, "${it.latitude} ${it.longitude}",Toast.LENGTH_SHORT)
            }
        }
    }



}