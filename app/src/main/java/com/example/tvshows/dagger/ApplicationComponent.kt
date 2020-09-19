package com.example.tvshows.dagger

import android.content.Context
import com.example.tvshows.MainActivity
import com.example.tvshows.dagger.module.*
import dagger.BindsInstance
import dagger.Component

import javax.inject.Singleton


@Singleton
@Component(modules = [
    ApiModule::class,
    DatabaseModule::class,
    MapperModule::class,
    ServiceModule::class,
    ViewModelModule::class])
interface ApplicationComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): ApplicationComponent
    }

    fun inject(activity: MainActivity)
}