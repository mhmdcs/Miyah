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

class BarFragment : Fragment() {

    private lateinit var binding: FragmentBarBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //initialize the data binding variable
        val binding: FragmentBarBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_bar,
            container,
            false)

        //inflate the fragment_bar.xml layout, might not be necessary since you inflate the view in the binding declaration above
        val view = inflater.inflate(R.layout.fragment_bar, container, false)

        var statusFb: Int //status Firebase
        var status: Int
        var max: String
        var maxI = 0
        var statusDepth: Int

        val sData: DatabaseReference = FirebaseDatabase.getInstance().reference
        val sensorData: TextView = binding.textViewProgress
        val imgDat: ImageView = binding.imgData
        val tank: EditText = binding.tankDepth
        val ok: Button = binding.okBtn

        ok.setOnClickListener {
            max = tank.text.toString()
            maxI = max.toInt()

            //define ValueEventListener to decide what happens when the data changes
            sData.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    statusFb = dataSnapshot.child("distance").value.toString()
                        .toInt() //get the necessary value from the database and parse it to integer, this is  the actual value of the water level
                    statusDepth =
                        maxI - statusFb //"max" is the sensor value of the (empty) tank is, meaning it's level 0
                    status =
                        (statusDepth.toDouble() / maxI.toDouble() * 100).toInt() //water level percentage calculation

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
        }




        /* updateProgressBar()


         binding.buttonIncr.setOnClickListener {
             if (progr <= 90) {
                 progr += 10
                 updateProgressBar()
             }
         }

         binding.buttonDecr.setOnClickListener {
             if (progr >= 10) {
                 progr -= 10
                 updateProgressBar()
             }
         }*/

        // inflate the layout for this fragment
        return binding.root
    }

    /* private var progr = 0

     private fun updateProgressBar() {
         binding.progressBar.progress = progr
         binding.textViewProgress.text = "$progr%"
     }*/
}