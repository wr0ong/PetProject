package com.hfad.pet3

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hfad.pet3.databinding.FragmentUsersBinding


class MovieAdapter : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {
    private var movieList = ArrayList<MovieResult>()
    fun setMovieList(movieList: List<MovieResult>) {
        this.movieList = movieList as ArrayList<MovieResult>
        notifyDataSetChanged()
    }


    class ViewHolder(val binding: FragmentUsersBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentUsersBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load("https://image.tmdb.org/t/p/w500" + movieList[position].poster_path)
            .into(holder.itemView.findViewById<ImageView>(R.id.movieImage))
        holder.itemView.findViewById<TextView>(R.id.movieName).text = movieList[position].title
    }

    override fun getItemCount(): Int {
        return movieList.size
    }
}