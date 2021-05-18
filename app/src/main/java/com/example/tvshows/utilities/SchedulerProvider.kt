package com.example.tvshows.utilities

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler

interface SchedulerProvider {
    fun io(): Scheduler
    fun main(): Scheduler
    fun interval(): Scheduler
}

class RuntimeSchedulerProvider : SchedulerProvider {
    override fun io(): Scheduler {
        return Schedulers.io()
    }

    override fun main(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    override fun interval(): Scheduler {
        return Schedulers.computation()
    }
}

class TestSchedulerProvider(private val testScheduler: TestScheduler = TestScheduler()) : SchedulerProvider {
    override fun io(): Scheduler {
        return Schedulers.trampoline()
    }

    override fun main(): Scheduler {
        return Schedulers.trampoline()
    }

    override fun interval(): Scheduler {
        return testScheduler
    }
}