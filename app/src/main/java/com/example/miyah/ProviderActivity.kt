package com.example.miyah

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.miyah.databinding.FragmentProviderBinding
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ProviderActivity : AppCompatActivity() {

    private lateinit var binding: FragmentProviderBinding
    private lateinit var database: DatabaseReference
    private lateinit var miyahAdapter: MiyahAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_provider)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_provider)


        database = FirebaseDatabase.getInstance().getReference().child("users")

        val clientsRecycler = binding.clientsRecycler
        // clientsRecycler.setLayoutManager(LinearLayoutManager(requireContext())) //already set this in the xml layout

        //fetch data from firebase and into the recycler view
        //https://firebaseopensource.com/projects/firebase/firebaseui-android/database/readme/

        val options: FirebaseRecyclerOptions<User> = FirebaseRecyclerOptions.Builder<User>()
            .setQuery(database, User::class.java)
            .build()

         val miyahAdapter = MiyahAdapter(options)
        clientsRecycler.adapter = miyahAdapter

    }

    override fun onStart() {
        super.onStart()
        miyahAdapter.startListening()

    }

    override fun onStop() {
        super.onStop()
        miyahAdapter.stopListening()

    }
}