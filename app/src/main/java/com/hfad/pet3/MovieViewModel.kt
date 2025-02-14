package com.hfad.pet3

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hfad.pet3.UsersFragment.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieViewModel private constructor(val apiKey: String) : ViewModel() {
    private var movieLiveData = MutableLiveData<List<MovieResult>>()
    fun getPopularMovies() {
        RetrofitInstance.api.getPopularMovies(apiKey).enqueue(object : Callback<Movie> {
            override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                if (response.body() != null) {
                    movieLiveData.value = response.body()!!.results
                } else {
                    return
                }
            }

            override fun onFailure(call: Call<Movie>, t: Throwable) {
                Log.d("TAG", t.message.toString())
            }
        })
    }

    fun observeMovieLiveData(): LiveData<List<MovieResult>> {
        return movieLiveData
    }
}
