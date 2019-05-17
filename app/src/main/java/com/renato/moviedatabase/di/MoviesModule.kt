package com.renato.moviedatabase.di

import com.renato.moviedatabase.api.ApiService
import com.renato.moviedatabase.schedulers.SchedulerProvider
import com.renato.moviedatabase.ui.DetailViewModel
import com.renato.moviedatabase.ui.MovieListViewModel
import com.renato.moviedatabase.ui.State
import com.renato.moviedatabase.usecases.GetMoviesUseCase
import com.renato.moviedatabase.usecases.GetMoviesUseCaseImpl
import dagger.Module
import dagger.Provides
import javax.inject.Provider

@Module
object MoviesModule {
    @Provides
    @JvmStatic
    fun provideUsecase(api: ApiService): GetMoviesUseCase =
        GetMoviesUseCaseImpl(api)

    @Provides
    @JvmStatic
    fun provideMovieDetailVM(
        schedulerProvider: SchedulerProvider
    ): DetailViewModel = DetailViewModel(
        null,
        schedulerProvider
    )

    @Provides
    @JvmStatic
    fun provideListVM(
        getPostsUseCase: GetMoviesUseCase,
        schedulerProvider: SchedulerProvider
    ): MovieListViewModel = MovieListViewModel(
        null,
        getPostsUseCase,
        schedulerProvider
    )

    @Provides
    @JvmStatic
    fun provideListVMFactory(vmProvider: Provider<MovieListViewModel>): ViewModelFactory<MovieListViewModel> =
        ViewModelFactory(vmProvider)

    @Provides
    @JvmStatic
    fun provideDetailsVMFactory(vmProvider: Provider<DetailViewModel>): ViewModelFactory<DetailViewModel> =
        ViewModelFactory(vmProvider)

}
