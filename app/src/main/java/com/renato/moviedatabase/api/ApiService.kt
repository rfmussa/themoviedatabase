package com.renato.moviedatabase.api

import com.renato.moviedatabase.data.model.ChangesResponse
import com.renato.moviedatabase.data.model.Movie
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("movie/changes")
    fun getChanges(@Query("start_date") date : String?): Single<ChangesResponse>

    @GET("movie/{id}")
    fun getMovies(@Path("id") id: String): Single<Movie>
}