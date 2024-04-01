package com.example.androidkotlindemo.fragment

import com.example.androidkotlindemo.R
import com.example.androidkotlindemo.activity.MainActivity
import com.example.androidkotlindemo.utils.CommonMethod
import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import java.time.LocalDateTime
import java.util.*

class DashboardFragment : Fragment(), View.OnClickListener {
    private lateinit var btnCurrentDate: Button
    private lateinit var btnGetLocation: Button
    private lateinit var btnRV: Button

    private val REQUEST_PERMISSION_REQUEST_CODE = 1234

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_dashboard, container, false)
        btnCurrentDate = view.findViewById(R.id.btn_date)
        btnGetLocation = view.findViewById(R.id.btn_getlocation)
        btnRV = view.findViewById(R.id.btn_rv)

        btnCurrentDate.setOnClickListener(this)
        btnGetLocation.setOnClickListener(this)
        btnRV.setOnClickListener(this)
        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_date -> {
                val current = LocalDateTime.now()
                val formatedDate = CommonMethod.dateFormate(current)
                Toast.makeText(
                    context,
                    "Current Date and Time is: $formatedDate",
                    Toast.LENGTH_SHORT
                ).show()
            }

            R.id.btn_getlocation -> {
                // Check Permission
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        REQUEST_PERMISSION_REQUEST_CODE
                    )
                } else {
                    getCurrentLocation()
                }
            }

            R.id.btn_rv -> {
                startActivity(Intent(requireContext(), MainActivity::class.java))
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_REQUEST_CODE && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation()
            } else {
                Toast.makeText(requireContext(), "Permission Denied!", Toast.LENGTH_SHORT).show()
            }
        }
    }


    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {

        val locationRequest = LocationRequest()
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        //now getting address from latitude and longitude

        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        var addresses: List<Address>

        LocationServices.getFusedLocationProviderClient(requireActivity())
            .requestLocationUpdates(locationRequest, object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    super.onLocationResult(locationResult)
                    LocationServices.getFusedLocationProviderClient(requireActivity())
                        .removeLocationUpdates(this)
                    if (locationResult != null && locationResult.locations.size > 0) {
                        val locIndex = locationResult.locations.size - 1

                        val latitude = locationResult.locations[locIndex].latitude
                        val longitude = locationResult.locations[locIndex].longitude

                        addresses = geocoder.getFromLocation(latitude, longitude, 1)!!

                        val address: String = addresses[0].getAddressLine(0)
                        Toast.makeText(
                            requireContext(),
                            "LAT: $latitude LONG: $longitude \n ADDRESS: $address",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }, Looper.getMainLooper())

    }
}