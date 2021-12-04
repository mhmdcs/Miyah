package com.example.miyah

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.miyah.databinding.FragmentClientBinding
import com.example.miyah.utils.sendNotification
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ClientFragment : Fragment() {

    companion object {
        val TAG: String = ClientFragment::class.java.simpleName
    }

    private lateinit var binding: FragmentClientBinding
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //initialize the data binding variable
         binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_client,
            container,
            false)

        var distance: Int //espDistance in Firebase
        val app = requireActivity().application //get the application context

        database = FirebaseDatabase.getInstance().reference //instance of the firebase database reference
        val sensorData: TextView = binding.textWaterPercentage
        val imgDat: ImageView = binding.imgWaterLevel

        Log.i(TAG, "Verify Firebase realtime database reference has been captured: $database")

        binding.requestWaterRefillButton.setOnClickListener {
            val intent = Intent(this@ClientFragment.requireContext(), MapsActivity::class.java)
            startActivity(intent)
        }

        //define ValueEventListener to decide what happens when the data changes
            database.addValueEventListener(object : ValueEventListener {

                //onDataChange method is called every time the data is changed
                override fun onDataChange(snapshot: DataSnapshot) { //fetch data using dataSnapshot object

                    val currentUser = FirebaseAuth.getInstance().uid

                    Log.i(TAG, "Captured sign in user unique id: $currentUser")

                    distance = snapshot.child("users/$currentUser/espDistance").value.toString()
                        .toInt() //get the distance value in cm from the database and parse it to String
                    sensorData.text = "100%"

                    //Using Kotlin's when statement. Similar to if-else, but more idiomatic.
                    when {
                        distance == 2 -> {imgDat.setImageResource(R.drawable.i100)
                            sensorData.text = "100%"
                            binding.requestWaterRefillButton.isVisible = false
                            FirebaseDatabase.getInstance().reference.child("users/$currentUser/requestStatus")
                                .setValue("false")}
                        distance == 3 -> {imgDat.setImageResource(R.drawable.i100)
                            sensorData.text = "100%"
                            binding.requestWaterRefillButton.isVisible = false
                            FirebaseDatabase.getInstance().reference.child("users/$currentUser/requestStatus")
                                .setValue("false")}
                        distance == 4 -> {imgDat.setImageResource(R.drawable.i93)
                            sensorData.text = "93%"
                            binding.requestWaterRefillButton.isVisible = false
                            FirebaseDatabase.getInstance().reference.child("users/$currentUser/requestStatus")
                                .setValue("false")}
                        distance == 5 -> {imgDat.setImageResource(R.drawable.i86)
                            sensorData.text = "86%"
                            binding.requestWaterRefillButton.isVisible = false
                            FirebaseDatabase.getInstance().reference.child("users/$currentUser/requestStatus")
                                .setValue("false")}
                        distance == 6 -> {imgDat.setImageResource(R.drawable.i79)
                            sensorData.text = "79%"
                            binding.requestWaterRefillButton.isVisible = false
                            FirebaseDatabase.getInstance().reference.child("users/$currentUser/requestStatus")
                                .setValue("false")}
                        distance == 7 -> {imgDat.setImageResource(R.drawable.i72)
                            sensorData.text = "72%"
                            binding.requestWaterRefillButton.isVisible = false
                            FirebaseDatabase.getInstance().reference.child("users/$currentUser/requestStatus")
                                .setValue("false")}
                        distance == 8 -> {imgDat.setImageResource(R.drawable.i65)
                            sensorData.text = "65%"
                            binding.requestWaterRefillButton.isVisible = false
                            FirebaseDatabase.getInstance().reference.child("users/$currentUser/requestStatus")
                                .setValue("false")}
                        distance == 9 -> {imgDat.setImageResource(R.drawable.i58)
                            sensorData.text = "58%"
                            binding.requestWaterRefillButton.isVisible = false
                            FirebaseDatabase.getInstance().reference.child("users/$currentUser/requestStatus")
                                .setValue("false")}
                        distance == 10 -> {imgDat.setImageResource(R.drawable.i51)
                            sensorData.text = "51%"
                            binding.requestWaterRefillButton.isVisible = true}
                        distance == 11 -> {imgDat.setImageResource(R.drawable.i44)
                            sensorData.text = "44%"
                            binding.requestWaterRefillButton.isVisible = true}
                        distance == 12 -> {imgDat.setImageResource(R.drawable.i37)
                            sensorData.text = "37%"
                            binding.requestWaterRefillButton.isVisible = true
                            val notificationManager = ContextCompat.getSystemService(
                                app, NotificationManager::class.java) as NotificationManager
                            notificationManager.sendNotification(app.getString(R.string.notification_text), app)}
                        distance == 13 -> {imgDat.setImageResource(R.drawable.i30)
                            sensorData.text = "30%"
                            binding.requestWaterRefillButton.isVisible = true}
                        distance == 14 -> {imgDat.setImageResource(R.drawable.i25)
                            sensorData.text = "25%"
                            binding.requestWaterRefillButton.isVisible = true}
                        distance == 15 -> {imgDat.setImageResource(R.drawable.i20)
                            sensorData.text = "20%"
                            binding.requestWaterRefillButton.isVisible = true}
                        distance == 16 -> {imgDat.setImageResource(R.drawable.i15)
                            sensorData.text = "15%"
                            binding.requestWaterRefillButton.isVisible = true
                            val notificationManager = ContextCompat.getSystemService(
                                app, NotificationManager::class.java) as NotificationManager
                            notificationManager.sendNotification(app.getString(R.string.notification_text), app)}
                        distance == 17 -> {imgDat.setImageResource(R.drawable.i10)
                            sensorData.text = "10%"
                            binding.requestWaterRefillButton.isVisible = true}
                        distance == 18 -> {imgDat.setImageResource(R.drawable.i0)
                            sensorData.text = "0%"
                            binding.requestWaterRefillButton.isVisible = true}
                        else -> {imgDat.setImageResource(R.drawable.i0)
                            binding.requestWaterRefillButton.isVisible = true
                            sensorData.text = "0%"}
                    }//end of when statement

                }// end of onDataChange method

                //onCancelled method is called if reading from the database is canceled.
                //For example, a read can be canceled if the client doesn't have permission to read from a Firebase database location
                //Optional to print errors here
                override fun onCancelled(databaseError: DatabaseError) {
                    Log.i(TAG,"Database connection error")
                }// end of onCancelled method
            })

        createChannel(
            getString(R.string.water_tank_notification_channel_id),
            getString(R.string.water_tank_notification_channel_name)
        )

        setHasOptionsMenu(true)

        return binding.root
    }




    private fun createChannel(channelId: String, channelName: String) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Water Level"

            val notificationManager = requireActivity().getSystemService(
                NotificationManager::class.java
            )

            notificationManager.createNotificationChannel(notificationChannel)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater!!.inflate(R.menu.dropdown_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.profileFragment -> NavigationUI.onNavDestinationSelected(item!!, requireView().findNavController())
            R.id.loginFragment -> { FirebaseAuth.getInstance().signOut()
                NavigationUI.onNavDestinationSelected(item!!, requireView().findNavController())}
        }
        return true
    }

}