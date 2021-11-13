package com.example.miyah

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.miyah.databinding.FragmentBarBinding
import com.example.miyah.utils.sendNotification
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class BarFragment : Fragment() {

    companion object {
        val TAG: String = BarFragment::class.java.simpleName
    }

    private lateinit var binding: FragmentBarBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
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

        var statusFb: Int //status Firebase
        var status: Int //status in displayed percentage format
        var max = 9 //maximum in Integer (converted) this is the tank's depth in cm
        var statusDepth: Int
        val app = requireActivity().application //get the application context

        database = FirebaseDatabase.getInstance().reference //instance of the firebase database reference
        val sensorData: TextView = binding.textWaterPercentage
        val imgDat: ImageView = binding.imgWaterLevel
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())


        binding.requestWaterRefillButton.setOnClickListener {
            val intent = Intent(this@BarFragment.requireContext(), MapsActivity::class.java)
            startActivity(intent)

       //     fetchLocation()
        }

        //define ValueEventListener to decide what happens when the data changes
            database.addValueEventListener(object : ValueEventListener {

                //onDataChange method is called every time the data is changed
                override fun onDataChange(snapshot: DataSnapshot) { //fetch data using dataSnapshot object



                    var currentUser = FirebaseAuth.getInstance().uid
                    statusFb = snapshot.child("users/"+currentUser+"/espDistance").value.toString()
                        //    statusFb = snapshot.child("distance").value.toString()
                        .toInt() //get the necessary value from the database and parse it to integer, this is  the actual value of the water level
                    statusDepth =
                        max - statusFb //"max" is the sensor value of the (empty) tank is, meaning it's level 0
                    status =
                        (statusDepth.toDouble() / max.toDouble() * 100).toInt() //water level percentage calculation


                    sensorData.text = Integer.toString(status) + " %" //parse the value to string to display it in the sensorData percentage

                    //Using Kotlin's when statement
                    when{
                        status >= 75 && status <= 80 -> {imgDat.setImageResource(R.drawable.i100)
                        binding.requestWaterRefillButton.isVisible = false
                            var currentUser = FirebaseAuth.getInstance().uid
                            FirebaseDatabase.getInstance().getReference().child("users/"+currentUser+"/requestStatus")
                                .setValue("false")}
                        status >= 70 && status < 75 -> {imgDat.setImageResource(R.drawable.i95)
                        binding.requestWaterRefillButton.isVisible = false
                            var currentUser = FirebaseAuth.getInstance().uid
                            FirebaseDatabase.getInstance().getReference().child("users/"+currentUser+"/requestStatus")
                                .setValue("false")}
                        status >= 65 && status < 70 -> {imgDat.setImageResource(R.drawable.i90)
                        binding.requestWaterRefillButton.isVisible = false
                            var currentUser = FirebaseAuth.getInstance().uid
                            FirebaseDatabase.getInstance().getReference().child("users/"+currentUser+"/requestStatus")
                                .setValue("false")}
                        status >= 60 && status < 65 -> {imgDat.setImageResource(R.drawable.i80)
                        binding.requestWaterRefillButton.isVisible = false
                            var currentUser = FirebaseAuth.getInstance().uid
                            FirebaseDatabase.getInstance().getReference().child("users/"+currentUser+"/requestStatus")
                                .setValue("false")}
                        status >= 55 && status < 60 -> {imgDat.setImageResource(R.drawable.i70)
                        binding.requestWaterRefillButton.isVisible = false
                            var currentUser = FirebaseAuth.getInstance().uid
                            FirebaseDatabase.getInstance().getReference().child("users/"+currentUser+"/requestStatus")
                                .setValue("false")}
                        status >= 50 && status < 55 -> {imgDat.setImageResource(R.drawable.i55)
                        binding.requestWaterRefillButton.isVisible = false
                            var currentUser = FirebaseAuth.getInstance().uid
                            FirebaseDatabase.getInstance().getReference().child("users/"+currentUser+"/requestStatus")
                                .setValue("false")}
                        status >= 40 && status < 50 -> {imgDat.setImageResource(R.drawable.i50)
                        binding.requestWaterRefillButton.isVisible = false
                            var currentUser = FirebaseAuth.getInstance().uid
                            FirebaseDatabase.getInstance().getReference().child("users/"+currentUser+"/requestStatus")
                                .setValue("false")
                        }
                        status >= 30 && status < 40 -> {imgDat.setImageResource(R.drawable.i40)
                            binding.requestWaterRefillButton.isVisible = true}
                        status >= 20 && status < 30 -> {imgDat.setImageResource(R.drawable.i30)
                        binding.requestWaterRefillButton.isVisible = true}
                        status >= 15 && status < 20 -> {imgDat.setImageResource(R.drawable.i20)
                            binding.requestWaterRefillButton.isVisible = true}
                        status >= 10 && status < 15 -> {imgDat.setImageResource(R.drawable.i15)
                                val notificationManager = ContextCompat.getSystemService(
                                app, NotificationManager::class.java) as NotificationManager
                                notificationManager.sendNotification(app.getString(R.string.notification_text), app)
                                 binding.requestWaterRefillButton.isVisible = true}
                        status > 0 && status < 10 -> {imgDat.setImageResource(R.drawable.i10)
                            binding.requestWaterRefillButton.isVisible = true}
                        status < 0 -> {imgDat.setImageResource(R.drawable.i0)
                            binding.requestWaterRefillButton.isVisible = true}
                              else -> {imgDat.setImageResource(R.drawable.i0)
                                  binding.requestWaterRefillButton.isVisible = true}
                    }

                }// end of onDataChange method

                override fun onCancelled(databaseError: DatabaseError) {}
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
                NotificationManager.IMPORTANCE_LOW
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
        FirebaseAuth.getInstance().signOut()
        return NavigationUI.onNavDestinationSelected(item!!, requireView().findNavController())
                || super.onOptionsItemSelected(item)
    }

    private fun fetchLocation() {
        val task = fusedLocationProviderClient.lastLocation
        var location: String

        if(ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(requireActivity(),arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 101)
            return
        }
        task.addOnSuccessListener {
            if(it != null){
                Toast.makeText(activity, "${it.latitude} ${it.longitude}", Toast.LENGTH_SHORT).show()
                Log.i(TAG,"${it.latitude} ${it.longitude}")
                location = it.latitude.toString()+","+it.longitude.toString()

                var currentUser = FirebaseAuth.getInstance().uid
                FirebaseDatabase.getInstance().getReference().child("users/"+currentUser+"/location")
                    .setValue(location)
                FirebaseDatabase.getInstance().getReference().child("users/"+currentUser+"/requestStatus")
                    .setValue("true")
                Log.i(TAG,currentUser.toString())
                Log.i(TAG,location)
            }
        }
        return
    }

}