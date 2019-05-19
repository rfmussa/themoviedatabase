package com.renato.moviedatabase.ui

import android.os.Parcelable
import com.renato.moviedatabase.data.model.Movie
import com.renato.moviedatabase.schedulers.SchedulerProvider
import com.renato.moviedatabase.usecases.GetMoviesUseCase
import com.ww.roxie.BaseAction
import com.ww.roxie.BaseState
import com.ww.roxie.BaseViewModel
import io.reactivex.rxkotlin.ofType
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.parcel.Parcelize

sealed class Action : BaseAction {
    data class LoadFeed(val startDate: Int?) : Action()
}

@Parcelize
data class State(
    val movies: List<Movie> = emptyList(),
    val isIdle: Boolean = false,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val startDate: Int? = 0
) : BaseState, Parcelable {
    fun forLoading() = copy(isIdle = false, isLoading = true, isError = false, movies = emptyList())
    fun forReceivedData(movies: List<Movie>, startDate: Int?) =
        copy(isLoading = false, movies = movies, startDate = startDate)

    fun forError() = copy(isLoading = false, isError = true)
}

sealed class Change {
    object Loading : Change()
    data class Movies(val movies: List<Movie>, val startDate: Int?) : Change()
    object Empty : Change()
}

class MovieListViewModel(
    initialState: State?,
    private val getMoviesUseCase: GetMoviesUseCase,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel<Action, State>() {

    override val initialState = initialState ?: State(isIdle = true)

    init {
        bindData()
    }

    private fun bindData() {
        disposables += actions.ofType<Action.LoadFeed>()
            .switchMap { action ->
                getMoviesUseCase.getMovies(action.startDate)
                    .toObservable()
                    .map<Change> {
                        Change.Movies(it, action.startDate)
                    }
                    .defaultIfEmpty(Change.Movies(emptyList(), action.startDate))
                    .startWith(Change.Loading)
                    .onErrorReturn { Change.Empty }
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
            }
            .scan(initialState) { state, change ->
                when (change) {
                    is Change.Loading -> state.forLoading()
                    is Change.Movies -> state.forReceivedData(change.movies, change.startDate)
                    is Change.Empty -> state.forError()
                }
            }
            .filter { !it.isIdle }
            .distinctUntilChanged()
            .observeOn(schedulerProvider.ui())
            .subscribe { state.value = it }
    }

}