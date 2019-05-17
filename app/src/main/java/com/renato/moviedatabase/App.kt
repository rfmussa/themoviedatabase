package com.renato.moviedatabase

import android.app.Application
import com.renato.moviedatabase.di.AppComponent
import com.renato.moviedatabase.di.DaggerAppComponent
import com.ww.roxie.Roxie

class App : Application(), AppComponentProvider {

    override val component: AppComponent by lazy {
        DaggerAppComponent.builder()
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        Roxie.enableLogging()
    }
}