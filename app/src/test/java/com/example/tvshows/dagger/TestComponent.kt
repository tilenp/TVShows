package com.example.tvshows.dagger

import android.content.Context
import com.example.tvshows.dagger.module.*
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    TestApplicationModule::class,
    TestViewModelModule::class])
interface TestComponent: AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): TestComponent
    }
}