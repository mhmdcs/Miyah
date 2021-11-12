package com.example.miyah

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.miyah.databinding.FragmentProviderBinding
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ValueEventListener

class ProviderFragment : Fragment() {

    private lateinit var binding: FragmentProviderBinding
    private lateinit var database: DatabaseReference
    private lateinit var miyahAdapter: MiyahAdapter

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

        miyahAdapter = MiyahAdapter(options)
        clientsRecycler.adapter = miyahAdapter

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        miyahAdapter.startListening()

    }

    override fun onStop() {
        super.onStop()
        miyahAdapter.stopListening()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater!!.inflate(R.menu.dropdown_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        FirebaseAuth.getInstance().signOut()
        return NavigationUI.onNavDestinationSelected(item!!, requireView().findNavController())
                || super.onOptionsItemSelected(item)
    }

}