package com.renato.moviedatabase.di

import com.renato.moviedatabase.ui.DetailViewModel
import com.renato.moviedatabase.ui.MovieListViewModel
import com.renato.moviedatabase.ui.State
import dagger.Component

@FragmentScope
@Component(dependencies = [AppComponent::class], modules = [MoviesModule::class])
interface MoviesComponent {
    fun provideMovieListVM(): ViewModelFactory<MovieListViewModel>
    fun provideDetailVM(): ViewModelFactory<DetailViewModel>
}