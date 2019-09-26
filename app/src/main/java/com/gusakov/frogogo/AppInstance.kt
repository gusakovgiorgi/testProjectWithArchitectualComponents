package com.gusakov.frogogo

import android.app.Application
import com.gusakov.frogogo.di.appModule
import com.gusakov.frogogo.di.networkModule
import com.gusakov.frogogo.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class AppInstance : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@AppInstance)
            modules(listOf(appModule, viewModelModule, networkModule))
        }
    }
}