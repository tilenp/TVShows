package com.example.tvshows.dagger

import android.content.Context
import androidx.paging.ExperimentalPagingApi
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
    AppModule::class,
    DatabaseModule::class,
    MapperModule::class,
    ServiceModule::class,
    ViewModelModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): AppComponent
    }

    fun inject(activity: MainActivity)

    @OptIn(ExperimentalPagingApi::class)
    fun inject(fragment: ShowSummariesFragment)
    fun inject(fragment: ShowDetailsFragment)
}