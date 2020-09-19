package com.example.tvshows.dagger

import android.content.Context
import com.example.tvshows.MainActivity
import com.example.tvshows.dagger.module.ApiModule
import com.example.tvshows.dagger.module.ServiceModule
import dagger.BindsInstance
import dagger.Component

import javax.inject.Singleton


@Singleton
@Component(modules = [
    ApiModule::class,
    DatabaseModule::class,
    ServiceModule::class,
    ViewModelModule::class])
interface ApplicationComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): ApplicationComponent
    }

    fun inject(activity: MainActivity)
}