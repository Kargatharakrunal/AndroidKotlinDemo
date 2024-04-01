package com.example.androidkotlindemo.fragment

import com.example.androidkotlindemo.R
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter

class DetailFragment : Fragment() {

    private val PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_detail, container, false)
        val permissionCheck = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            proceedToWriteFile()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
            )
        }
        return view
    }

    private fun proceedToWriteFile() {
        val fileName = "Demo.txt"
        val content = "hello this is demo content!"
        writeFileExternalStorage(content, requireContext(), fileName)
    }

    private fun writeFileExternalStorage(strWrite: String, context: Context, fileName: String) {
        try {
            if (isSdReadable()) {
                val fullPath = Environment.getExternalStorageDirectory().absolutePath
                val myFile = File(fullPath + File.separator + "/MyDoc/" + fileName)
                Log.d("TAG", myFile.toString())
                val fOut = FileOutputStream(myFile)
                val myOutWriter = OutputStreamWriter(fOut)
                myOutWriter.append(strWrite)
                myOutWriter.close()
                fOut.close()
                Toast.makeText(context, "File Written to external memory", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Error occurred in writing File", Toast.LENGTH_LONG).show()
        }
    }

    private fun isSdReadable(): Boolean {
        var mExternalStorageAvailable = false
        try {
            val state = Environment.getExternalStorageState()
            when {
                Environment.MEDIA_MOUNTED == state -> {
                    // We can read and write the media
                    mExternalStorageAvailable = true
                    Log.i("isSdReadable", "External storage card is readable.")
                }
                Environment.MEDIA_MOUNTED_READ_ONLY == state -> {
                    // We can only read the media
                    Log.i("isSdReadable", "External storage card is readable.")
                    mExternalStorageAvailable = true
                }
                else -> {
                    // Something else is wrong. It may be
                    // one of many other states, but all
                    // we need to know is we can neither
                    // read nor write
                    mExternalStorageAvailable = false
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return mExternalStorageAvailable
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    proceedToWriteFile()
                } else {
                    // permission denied
                }
                return
            }
        }
    }

}