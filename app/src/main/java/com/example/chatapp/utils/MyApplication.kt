package com.example.chatapp.utils

import android.app.Application
import com.example.chatapp.modules.*
import org.koin.core.context.startKoin

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        AppPrefs.initiate(applicationContext)

        startKoin {
            modules(
                splashViewModelModule,
                loginViewModelModule,
                forgetPasswordViewModelModule,
                dashboardViewModelModule,
                chatViewModelModule,
                apiRepositoryModule
            )
        }
    }

}