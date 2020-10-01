package com.example.tvshows.dagger

import android.content.Context
import com.example.tvshows.dagger.module.*
import com.example.tvshows.ui.MainActivity
import com.example.tvshows.ui.showdetails.ShowDetailsFragment
import com.example.tvshows.ui.showslist.ShowSummariesFragment
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

    fun inject(fragment: ShowSummariesFragment)
    fun inject(fragment: ShowDetailsFragment)
}