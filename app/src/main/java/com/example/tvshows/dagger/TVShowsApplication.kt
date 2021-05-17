package com.example.tvshows.dagger

import android.app.Application


class TVShowsApplication : Application(), ComponentProvider {

    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this)
    }

    override fun provideAppComponent(): AppComponent {
        return appComponent
    }
}