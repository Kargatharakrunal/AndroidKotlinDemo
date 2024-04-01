package com.example.androidkotlindemo.adapter

import com.example.androidkotlindemo.R
import com.example.androidkotlindemo.model.Movie
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class RecyclerAdapter(
    var context: Context,
    private var movieList: List<Movie>?) : RecyclerView.Adapter<RecyclerAdapter.MyviewHolder>() {
    @SuppressLint("NotifyDataSetChanged")
    fun setMovieList(movieList: List<Movie>?) {
        this.movieList = movieList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyviewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.recyclerview_adapter, parent, false)
        return MyviewHolder(view)
    }

    override fun onBindViewHolder(holder: MyviewHolder, position: Int) {
        holder.tvMovieName.text = movieList!![position].title

//        Glide.with(context).load(Uri.parse(movieList.get(position).getImageUrl())).into(holder.image);
        Picasso.get().load(movieList!![position].imageUrl).into(holder.image)
    }

    override fun getItemCount(): Int {
        return if (movieList != null) {
            movieList!!.size
        } else 0
    }

    inner class MyviewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.image)
        var tvMovieName: TextView = itemView.findViewById(R.id.title)

    }

}