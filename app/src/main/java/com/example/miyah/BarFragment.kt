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
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class BarFragment : Fragment() {

    private lateinit var binding: FragmentBarBinding
    private lateinit var database: DatabaseReference
    val TAG = "LogBarFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //initialize the data binding variable
         binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_bar,
            container,
            false)

        var status: String
        database = FirebaseDatabase.getInstance().reference

        database.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
            status = snapshot.child("distances").value.toString()
                binding.textWaterPercentage.text = status
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })

        return binding.root
    }
}