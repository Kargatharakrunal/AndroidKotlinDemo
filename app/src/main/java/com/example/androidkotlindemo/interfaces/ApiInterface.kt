package com.example.androidkotlindemo.interfaces

import com.example.androidkotlindemo.model.Movie
import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {
    @get:GET("photos/")
    val getMovies: Call<List<Movie>>
}