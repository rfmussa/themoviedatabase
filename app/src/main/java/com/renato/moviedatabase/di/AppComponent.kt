package com.renato.moviedatabase.di

import com.renato.moviedatabase.api.ApiService
import com.renato.moviedatabase.schedulers.SchedulerProvider
import com.renato.moviedatabase.usecases.GetMoviesUseCase
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, MoviesModule::class])
interface AppComponent {
    fun feedApiDefinition(): ApiService
    fun scheduler(): SchedulerProvider

}