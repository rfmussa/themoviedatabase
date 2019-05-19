package com.renato.moviedatabase.usecases

import android.util.Log
import com.renato.moviedatabase.api.ApiService
import com.renato.moviedatabase.data.model.Movie
import io.reactivex.Single
import khronos.Dates
import khronos.days
import khronos.minus
import khronos.toString

interface GetMoviesUseCase {
    fun getMovies(startDate: Int?): Single<List<Movie>>
}

class GetMoviesUseCaseImpl(
    private val apiService: ApiService
) : GetMoviesUseCase {

    override fun getMovies(startDate: Int?): Single<List<Movie>> = apiService.getChanges(formatDate(startDate))
        .map { data ->
            return@map data.results.filter { (it.adult != null && it.adult == false) }.take(12)
        }
        .toObservable()
        .flatMapIterable { it }
        .concatMap {
            apiService.getMovies(it.id).toObservable()
                .doOnError { t: Throwable? -> Log.d("throw", t!!.localizedMessage) }
        }
        .toList()
}

fun formatDate(date: Int?): String = (Dates.today - (date?.days ?: 0.days)).toString("YYYYMMdd")

