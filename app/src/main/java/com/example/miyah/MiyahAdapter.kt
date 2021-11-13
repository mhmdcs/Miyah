package com.example.miyah

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.miyah.databinding.ClientItemBinding
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import androidx.core.content.ContextCompat.startActivity

import android.content.Intent
import androidx.core.content.ContextCompat


class MiyahAdapter(options: FirebaseRecyclerOptions<User>) :
    FirebaseRecyclerAdapter<User, MiyahAdapter.ClientsViewHolder>(options) {

    class ClientsViewHolder(private val binding: ClientItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val name = binding.clientName
        val phone = binding.clientPhone
        val locationImage = binding.clientLocationImage

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientsViewHolder {
        return ClientsViewHolder(ClientItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ClientsViewHolder, position: Int, model: User) {
        holder.name.text = model.name
        holder.phone.text = model.phone
//        val uriString = "https://www.google.com/maps/search/?api=1&query="+model.location
//        val locationUri = uriString.toUri()
//        holder.locationImage.setImageURI(locationUri)

        holder.locationImage.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                val context = holder.locationImage.context
                val url = "https://www.google.com/maps/search/?api=1&query="+model.location
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                context.startActivity(intent)
            }

        })

    }

}