package com.example.tvshows.dagger

import android.content.Context
import com.example.tvshows.dagger.module.*
import com.example.tvshows.ui.showdetails.ShowDetailsFragmentTest
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    ApiModule::class,
    TestApplicationModule::class,
    DatabaseModule::class,
    MapperModule::class,
    ServiceModule::class,
    TestViewModelModule::class])
interface TestComponent: ApplicationComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): TestComponent
    }

    fun inject(testFragment: ShowDetailsFragmentTest)
}