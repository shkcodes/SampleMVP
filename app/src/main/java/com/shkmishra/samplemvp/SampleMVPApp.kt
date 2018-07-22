package com.shkmishra.samplemvp

import android.app.Application
import com.shkmishra.samplemvp.di.AppComponent
import com.shkmishra.samplemvp.di.AppModule
import com.shkmishra.samplemvp.di.DaggerAppComponent

class SampleMVPApp : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }

}