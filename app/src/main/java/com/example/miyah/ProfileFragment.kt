package com.example.miyah

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.miyah.databinding.FragmentProfileBinding
import com.example.miyah.databinding.FragmentProviderBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class ProfileFragment : Fragment() {

    companion object {
        val TAG: String = ProfileFragment::class.java.simpleName
    }

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //initialize the data binding variable
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_profile,
            container,
            false)


        binding.updateButton.setOnClickListener {
            it.findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToLoginFragment())
            var name = binding.nameEditTextEditProfile.text.toString().trim()
            var phone = binding.PhoneEditTextEditProfile.text.toString().trim()
            updateProfile(name,phone)
            FirebaseAuth.getInstance().signOut()
        }

        return binding.root
    }

    private fun updateProfile(name: String, phone: String) {
        var currentUser = FirebaseAuth.getInstance().uid.toString()
        Log.i(TAG, currentUser)
        var databaseReference = FirebaseDatabase.getInstance().getReference("users").child(currentUser)
        var user = mapOf(
            "name" to name,
            "phone" to phone)

        databaseReference.updateChildren(user).addOnSuccessListener {
            Toast.makeText(activity,"Successfully updated profile",Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(activity,"Failed to update profile",Toast.LENGTH_SHORT).show()
        }
    }

}