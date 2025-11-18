package com.cbgm.videochallenge

import android.app.Application
import androidx.work.Configuration
import com.cbgm.persistence.di.persistenceModule
import com.cbgm.videochallenge.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.factory.KoinWorkerFactory
import org.koin.core.Koin
import org.koin.core.context.GlobalContext.startKoin

class VideoApplication : Application(), Configuration.Provider {

    override fun onCreate() {
        super.onCreate()
        setupDI()
    }

    private fun setupDI(): Koin =
        startKoin {
            androidContext(applicationContext)
            modules(appModule, persistenceModule)
        }.koin

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder().setWorkerFactory(KoinWorkerFactory()).build()
}