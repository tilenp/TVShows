package com.example.tvshows.dagger

import android.content.Context
import com.example.tvshows.dagger.module.*
import com.example.tvshows.ui.showslist.MainActivity
import com.example.tvshows.ui.showslist.ShowDetailsFragment
import com.example.tvshows.ui.showslist.ShowsFragment
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

    fun inject(fragment: ShowsFragment)
    fun inject(fragment: ShowDetailsFragment)
}