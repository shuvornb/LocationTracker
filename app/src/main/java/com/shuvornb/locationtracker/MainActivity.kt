package com.shuvornb.locationtracker

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity:AppCompatActivity() {
     private var client: FusedLocationProviderClient? = null

     override fun onCreate(savedInstanceState:Bundle?) {
         super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_main)

         requestPermission()

         client = LocationServices.getFusedLocationProviderClient(this)
         locBtn.setOnClickListener {
             if (ActivityCompat.checkSelfPermission(this@MainActivity, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                 return@setOnClickListener
             }

             if (ActivityCompat.checkSelfPermission(this@MainActivity, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                 return@setOnClickListener
             }

             client!!.lastLocation.addOnSuccessListener(this@MainActivity) { location ->
                 if (location != null) {
                     txtViewDisplayLoc.setText(location!!.toString())
                 }
                 else {
                     txtViewDisplayLoc.setText("No data found")
                 }
             }


             val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
             val criteria = Criteria()
             val bestProvider = locationManager.getBestProvider(criteria, false)
             val location = locationManager.getLastKnownLocation(bestProvider!!)
             try {
                 val str = location!!.latitude.toString() + " " + location.longitude.toString()
                 txtViewDisplayLoc.text = str
             } catch (e: NullPointerException) {
                 Toast.makeText(this, "No location data found", Toast.LENGTH_SHORT).show()
             }


         }

     }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION),
            1
        )
    }

}
