package com.example.androidkotlindemo.model

import com.google.gson.annotations.SerializedName

class Movie(
    @field:SerializedName("title") var title: String,
    @field:SerializedName("url") var imageUrl: String
) 