package com.renato.moviedatabase.ui

import com.renato.moviedatabase.data.model.Movie
import com.renato.moviedatabase.schedulers.SchedulerProvider
import com.ww.roxie.BaseAction
import com.ww.roxie.BaseState
import com.ww.roxie.BaseViewModel
import io.reactivex.rxkotlin.ofType
import io.reactivex.rxkotlin.plusAssign

sealed class DetailAction : BaseAction {
    data class LoadMovie(val movie: Movie) : DetailAction()
}

data class DetailState(
    val movie: Movie? = null,
    val isIdle: Boolean = false
) : BaseState {
    fun forMovie(movie: Movie?) = copy(movie = movie)
}

sealed class DetailChange {
    data class ShowMovie(val movie: Movie) : DetailChange()
}

class DetailViewModel(
    initialState: DetailState?,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel<DetailAction, DetailState>() {

    override val initialState = initialState ?: DetailState(isIdle = true, movie = null)

    init {
        bindData()
    }

    private fun bindData() {
        disposables += actions.ofType<DetailAction.LoadMovie>()
            .map { action -> DetailChange.ShowMovie(action.movie) }
            .onErrorReturn { DetailChange.ShowMovie(Movie()) }
            .scan(initialState) { state, change ->
                state.forMovie(change.movie)
            }
            .observeOn(schedulerProvider.ui())
            .subscribe { state.value = it }
    }
}