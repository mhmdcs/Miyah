package com.example.miyah

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.miyah.databinding.FragmentProviderBinding
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ValueEventListener

class ProviderFragment : Fragment() {

    private lateinit var binding: FragmentProviderBinding
    private lateinit var users: LiveData<List<User>>
    private lateinit var database: DatabaseReference

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

        val app = requireActivity().application //get the application context
        database = FirebaseDatabase.getInstance().getReference().child("users")

        val clientsRecycler = binding.clientsRecycler
       // clientsRecycler.setLayoutManager(LinearLayoutManager(requireContext())) //already set this in the xml layout

        //fetch data from firebase and into the recycler view
        //https://firebaseopensource.com/projects/firebase/firebaseui-android/database/readme/

        val options: FirebaseRecyclerOptions<User> = FirebaseRecyclerOptions.Builder<User>()
            .setQuery(database, User::class.java)
            .build()

        var miyahAdapter = MiyahAdapter(options)

        clientsRecycler.adapter = miyahAdapter


        return binding.root
    }

    override fun onStart() {
        super.onStart()



      //  Log.i(TAG,miyahAdapter)
    }

    override fun onStop() {
        super.onStop()
    }
}