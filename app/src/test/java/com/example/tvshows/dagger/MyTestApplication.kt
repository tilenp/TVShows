package com.example.tvshows.dagger

import android.app.Application

class MyTestApplication : Application(), ComponentProvider {

    private lateinit var testComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        testComponent = DaggerTestComponent.factory().create(this)
    }

    override fun provideAppComponent(): AppComponent {
        return testComponent
    }
}