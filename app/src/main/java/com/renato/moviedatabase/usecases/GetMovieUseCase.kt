package com.renato.moviedatabase.usecases

import com.renato.moviedatabase.api.ApiService
import com.renato.moviedatabase.data.model.Movie
import io.reactivex.Single

interface GetMoviesUseCase {
    fun getMovies(startDate : String?): Single<List<Movie>>
}

class GetMoviesUseCaseImpl(
    private val apiService: ApiService
) : GetMoviesUseCase {

    override fun getMovies(startDate : String?): Single<List<Movie>> = apiService.getChanges(startDate)
        .map { data ->
            return@map data.results.filter { (it.adult != null && it.adult == false) }.take(12)
        }
        .toObservable()
        .flatMapIterable { it }
        .concatMap { apiService.getMovies(it.id).toObservable() }
        .toList()
}
