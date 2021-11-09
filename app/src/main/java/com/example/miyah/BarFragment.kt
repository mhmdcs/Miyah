package com.example.miyah

import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
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
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.setContentView
import com.example.miyah.databinding.FragmentBarBinding
import com.example.miyah.utils.sendNotification
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

        var statusFb: Int //status Firebase
        var status: Int //status in displayed percentage format
        var max = 9 //maximum in Integer (converted) this is the tank's depth in cm
        var statusDepth: Int
        val app = requireActivity().application //get the application context

        val databaseRef: DatabaseReference = FirebaseDatabase.getInstance().reference //instance of the firebase database reference
        val sensorData: TextView = binding.textWaterPercentage
        val imgDat: ImageView = binding.imgWaterLevel


        //define ValueEventListener to decide what happens when the data changes
            databaseRef.addValueEventListener(object : ValueEventListener {

                //onDataChange method is called every time the data is changed
                override fun onDataChange(dataSnapshot: DataSnapshot) { //fetch data using dataSnapshot object
                    statusFb = dataSnapshot.child("distance").value.toString()
                        .toInt() //get the necessary value from the database and parse it to integer, this is  the actual value of the water level
                    statusDepth =
                        max - statusFb //"max" is the sensor value of the (empty) tank is, meaning it's level 0
                    status =
                        (statusDepth.toDouble() / max.toDouble() * 100).toInt() //water level percentage calculation

                    sensorData.text =
                        Integer.toString(status) + " %" //parse the value to string to display it in the sensorData percentage

                    //change ImageViews based on changes in the water level percentage
                    if (status >= 95 && status <= 100) {
                        imgDat.setImageResource(R.drawable.i100)
                    } else if (status >= 90 && status < 95) {
                        imgDat.setImageResource(R.drawable.i95)
                    } else if (status >= 80 && status < 90) {
                        imgDat.setImageResource(R.drawable.i90)
                    } else if (status >= 70 && status < 80) {
                        imgDat.setImageResource(R.drawable.i80)
                    } else if (status >= 55 && status < 70) {
                        imgDat.setImageResource(R.drawable.i70)
                    } else if (status >= 50 && status < 55) {
                        imgDat.setImageResource(R.drawable.i55)
                    } else if (status >= 40 && status < 50) {
                        imgDat.setImageResource(R.drawable.i50)
                    } else if (status >= 30 && status < 40) {
                        imgDat.setImageResource(R.drawable.i40)
                    } else if (status >= 20 && status < 30) {
                        imgDat.setImageResource(R.drawable.i30)
                    } else if (status >= 15 && status < 20) {
                        imgDat.setImageResource(R.drawable.i20)
                    } else if (status >= 10 && status < 15) {
                        imgDat.setImageResource(R.drawable.i15)

// trigger a notification here when the water tank level is low. In order to call the
// sendNotification() function you previously implemented, you need an instance of NotificationManager.
// NotificationManager is a system service which provides all the functions exposed for notifications api,
// including the extension function you added. Anytime you want to send, cancel or update a notification
// you need to request an instance of the NotificationManager from the system.
// Call the sendNotification() extension function with the notification message and with the context.

                        val notificationManager = ContextCompat.getSystemService(
                            app, NotificationManager::class.java) as NotificationManager
                        notificationManager.sendNotification(app.getString(R.string.notification_text), app)

                    } else if (status > 0 && status < 10) {
                        imgDat.setImageResource(R.drawable.i10)
                    } else if (status < 0) {
                        imgDat.setImageResource(R.drawable.i0)
                        sensorData.text = ""
                    } else {
                        imgDat.setImageResource(R.drawable.i0)
                        sensorData.text = ""
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })


        createChannel(
            getString(R.string.water_tank_notification_channel_id),
            getString(R.string.water_tank_notification_channel_name)
        )

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
            notificationChannel.description = "Time for breakfast"

            val notificationManager = requireActivity().getSystemService(
                NotificationManager::class.java
            )

            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

}