package com.example.androidkotlindemo.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

object CommonMethod {
    private lateinit var bitmapPhoto: Bitmap
    private val stream = ByteArrayOutputStream()
    private const val PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1


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

    private fun writeFileExternalStorage(strWrite: String, context: Context, fileName: String) {
        try {
            if (isSdReadable()) {
                val fullPath = Environment.getExternalStorageDirectory().absolutePath
                val myFile = File(fullPath + File.separator + "/" + fileName)
                if (!myFile.exists()) {
                    try {
                        myFile.createNewFile()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
                try {
                    // BufferedWriter for performance, true to set append to file flag
                    val buf = BufferedWriter(FileWriter(myFile, true))
                    buf.append(strWrite)
                    buf.newLine()
                    buf.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                Toast.makeText(context, "File Written to external memory", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Error occurred in writing File", Toast.LENGTH_LONG).show()
        }
    }

    fun proceedToBASE6eFile(context: Context,data: Intent?){
        Log.d("TAG_IMAGE", data?.extras.toString())
        bitmapPhoto = data?.extras?.get("data") as Bitmap
        bitmapPhoto.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val bytes = stream.toByteArray()
        val strPhoto = Base64.encodeToString(bytes, Base64.DEFAULT)
        val strPhoto1 = MediaStore.Images.Media.insertImage(context.contentResolver, bitmapPhoto, "", null)
        val tempUri = Uri.parse(strPhoto1)
        Log.d("TAG_BASE64_PHOTO", strPhoto)
        Log.d("TAG_URI_PHOTO", tempUri.toString())
//        ivPhoto.setImageBitmap(bitmapPhoto)
        val permissionCheck = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            proceedToWriteFile(context, "-> Image $tempUri \n ")
        } else {
            ActivityCompat.requestPermissions(
                Activity(),
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
            )
        }
    }

    private fun proceedToWriteFile(context: Context,strPhotos: String) {
        val fileName = "Demo.txt"
        writeFileExternalStorage(strPhotos, context, fileName)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun dateFormate(strCurrentDate: LocalDateTime): String {
        val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
        return strCurrentDate.format(formatter)
    }
}