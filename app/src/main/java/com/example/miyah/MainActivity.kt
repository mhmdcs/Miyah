package com.example.miyah

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.miyah.databinding.ActivityMainBinding
import com.firebase.ui.database.FirebaseRecyclerOptions


class MainActivity : AppCompatActivity() {

    companion object {
        val TAG: String = MainActivity::class.java.simpleName  //    var TAG = "MainActivity"
    }

    //lateinit late initialization
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfig: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        //initialize the drawerLayout from the binding variable
        drawerLayout = binding.drawyerLayout
        val navController = this.findNavController(R.id.nav_host_fragment)
        appBarConfig = AppBarConfiguration(navController.graph, drawerLayout)

        //creating the Up button UI and linking it up with the Action Bar
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        //hook the navigation UI up to the navigation view by calling setupWithNavController
        NavigationUI.setupWithNavController(binding.navView, navController)


        //call addOnDestinationChangedListener with a lambda that sets the DrawerLockMode depending on what destination the app is navigating to.
        //When the id of the NavDestination matches the startDestination of the graph
        //this will unlock the drawerLayout; otherwise, it’ll lock and close the drawerLayout.
        //this lambda/anonymous function prevent nav gesture if not on start destination, it gets called EVERY TIME the destination changes, it gets back the destination
        navController.addOnDestinationChangedListener() { nc: NavController, nd: NavDestination, args: Bundle? ->

            if (nd.id == nc.graph.startDestination) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            } else {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }

        }


    }

    // this is like setting an onClickListener to the Up button, without it, clicking it doesn't do anything
    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.nav_host_fragment)

        return NavigationUI.navigateUp(
            navController,
            appBarConfig
        ) //use drawerLayout or appBarConfig here, both work same

        //return navController.navigateUp()
    }

    override fun onDestroy() {
        Log.i(TAG, "onDestroy called on MainActivity")
        super.onDestroy()
    }
}