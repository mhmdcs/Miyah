package com.example.miyah

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.miyah.databinding.ClientItemBinding
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class MiyahAdapter(options: FirebaseRecyclerOptions<User>) :
    FirebaseRecyclerAdapter<User, MiyahAdapter.ClientsViewHolder>(options) {

    class ClientsViewHolder(private val binding: ClientItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val name = binding.clientName
        val phone = binding.clientPhone
        val location = binding.clientLocation
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientsViewHolder {
        return ClientsViewHolder(ClientItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ClientsViewHolder, position: Int, model: User) {
        holder.name.text = model.name
        holder.phone.text = model.phone
        holder.location.text = model.location
    }

}