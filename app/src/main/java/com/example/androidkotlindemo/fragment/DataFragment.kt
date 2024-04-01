package com.example.androidkotlindemo.fragment

import com.example.androidkotlindemo.R
import com.example.androidkotlindemo.utils.CommonMethod
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class DataFragment : Fragment(), View.OnClickListener {
    private val cameraRequest = 1111
    private lateinit var ivPhoto: ImageView
    private lateinit var btnPhoto: Button
    private lateinit var cameraIntent: Intent

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_data, container, false)

        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                cameraRequest
            )
            ivPhoto = view.findViewById(R.id.iv_photo)
            btnPhoto = view.findViewById(R.id.btn_photo)

            btnPhoto.setOnClickListener(this)
        }
        return view
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_photo -> {

                cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, cameraRequest)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == cameraRequest) {
            CommonMethod.proceedToBASE6eFile(requireContext(), data)
        }
    }
}