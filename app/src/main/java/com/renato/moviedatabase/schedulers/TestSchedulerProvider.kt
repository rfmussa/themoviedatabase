package com.renato.moviedatabase.schedulers

import com.renato.moviedatabase.schedulers.SchedulerProvider
import io.reactivex.schedulers.TestScheduler

class TestSchedulerProvider(private val scheduler: TestScheduler) :
    SchedulerProvider {
	override fun computation() = scheduler
	override fun ui() = scheduler
	override fun io() = scheduler
}