package com.renato.moviedatabase

import androidx.fragment.app.Fragment
import com.renato.moviedatabase.di.AppComponent

interface AppComponentProvider {
    val component: AppComponent
}

val Fragment.appComponent get() = (activity!!.application as AppComponentProvider).component